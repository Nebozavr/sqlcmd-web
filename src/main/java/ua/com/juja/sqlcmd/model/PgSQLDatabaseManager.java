package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.utils.PropertiesLoader;

import java.sql.*;
import java.util.*;

public class PgSQLDatabaseManager implements DatabaseManager {

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String HOST = propertiesLoader.getServerName();
    private static final String PORT = propertiesLoader.getDatabasePort();
    private static final String DRIVER = propertiesLoader.getDriver();
    private static final String USERNAME = propertiesLoader.getUserName();
    private static final String PASSWORD = propertiesLoader.getPassword();
    private static final String LOGGER_LEVEL = propertiesLoader.getLoggerLevel();
    private static final String DATABASE_URL = DRIVER + HOST + ":" + PORT + "/";

    private final String lineSeparator = System.getProperty("line.separator");

    private Connection connection;

    @Override
    public void connect(String database, String userName, String password) throws PgSQLDatabaseManagerException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new PgSQLDatabaseManagerException("Please add JDBC jar to you project");
        }
        try {
            connection = DriverManager.getConnection(DATABASE_URL + database + LOGGER_LEVEL, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new PgSQLDatabaseManagerException(String.format("Can't get connection for database:" +
                    " %s ", database) + lineSeparator + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws PgSQLDatabaseManagerException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new PgSQLDatabaseManagerException("Can't close connection for database:" + lineSeparator + e.getMessage());
            }
            connection = null;
        } else {
            throw new PgSQLDatabaseManagerException("Disconnect failed. You are not connected to any Database");
        }
    }

    @Override
    public Set<String> listTables() throws PgSQLDatabaseManagerException {
        String[] types = {"TABLE"};
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }

        try (ResultSet tables = md.getTables(null, "public", "%", types)) {
            Set<String> result = new LinkedHashSet<>();

            while (tables.next()) {
                result.add(tables.getString(3));
            }

            return result;
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void clearTable(String tableName) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DELETE FROM %s", tableName);

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void dropTable(String tableName) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DROP TABLE %s", tableName);

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }


    @Override
    public void dropDatabase(String dbName) throws PgSQLDatabaseManagerException {
        try {
            connection = DriverManager.getConnection(DATABASE_URL + LOGGER_LEVEL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            connection = null;
            throw new PgSQLDatabaseManagerException(String.format("Can't get connection for database:") + lineSeparator + e.getMessage());
        }

        String checkConnect = String.format("SELECT * FROM pg_stat_activity WHERE datname = '%s'", dbName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkConnect)) {
            String sql = String.format("DROP DATABASE %s", dbName);

            if (resultSet.next()){
                throw new PgSQLDatabaseManagerException("Cannot drop the currently open database. Please disconnect.");
            }
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void dropAllTable() throws PgSQLDatabaseManagerException {
        Set<String> tables = listTables();
        for (String tableName : tables
                ) {
            dropTable(tableName);
        }
    }

    @Override
    public void createTable(String tableName, String... columns) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String column : columns) {
                sql.append(column + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(")");
            statement.executeUpdate(String.valueOf(sql));
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void createDataBase(String dbName) throws PgSQLDatabaseManagerException {
        try {
            connection = DriverManager.getConnection(DATABASE_URL + LOGGER_LEVEL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            connection = null;
            throw new PgSQLDatabaseManagerException(String.format("Can't get connection for database:") + lineSeparator + e.getMessage());
        }

        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE DATABASE " + dbName + " WITH ENCODING='UTF8'";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public List<DataSet> findData(String tableName) throws PgSQLDatabaseManagerException {
        String sql = String.format("SELECT * FROM %s", tableName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            resultSetMetaData.getColumnName(1);
            List<DataSet> result = new LinkedList<>();

            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                result.add(dataSet);
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    dataSet.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void insertData(String tableName, DataSet input) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {
            String columnNames = getNameFormatted(input);
            String values = getValuesFormatted(input);
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +
                    "VALUES (" + values + ")";

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {

            String columnWhere = getNameFormatted(dataWhere);
            String valuesWhere = getValuesFormatted(dataWhere);
            String columnSet = getNameFormatted(dataSet);
            String valuesSet = getValuesFormatted(dataSet);

            checkRows(tableName, columnWhere, valuesWhere);

            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = " + valuesSet + " " +
                    "WHERE " + columnWhere + " = " + valuesWhere;

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void deleteRecords(String tableName, DataSet input) throws PgSQLDatabaseManagerException {
        try (Statement statement = connection.createStatement()) {

            String column = getNameFormatted(input);
            String value = getValuesFormatted(input);

            checkRows(tableName, column, value);

            String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, column, value);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public Set<String> getTableColumnsNames(String tableName) throws PgSQLDatabaseManagerException {
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }

        try (ResultSet tables = md.getColumns(null, null, tableName, null)) {
            Set<String> result = new HashSet<>();

            while (tables.next()) {
                result.add(tables.getString("column_name"));
            }

            return result;
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public boolean hasTable(String tableName) throws PgSQLDatabaseManagerException {
        for (String name : listTables()) {
            if (name.equals(tableName)) return true;
        }

        return false;
    }

    private void checkRows(String tableName, String column, String value) throws PgSQLDatabaseManagerException {

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", tableName, column, value);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (!resultSet.next()) {
                final String message = String.format("Request was not execute, " +
                        "because the fields with this value (%s = %s) are not in the table %s", column, value, tableName);
                throw new PgSQLDatabaseManagerException(message);
            }
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    private String getValuesFormatted(DataSet input) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format("'%s',", value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String getNameFormatted(DataSet newValue) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format("%s,", name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
