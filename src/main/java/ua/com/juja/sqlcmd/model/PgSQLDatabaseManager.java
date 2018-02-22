package ua.com.juja.sqlcmd.model;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.utils.PropertiesLoader;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.collectionToDelimitedString;

@Component
@Scope(value = "prototype")
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
    private JdbcTemplate template; //= new JdbcTemplate(new SingleConnectionDataSource(connection, false));

    @Override
    public void connect(String database, String userName, String password) throws PgSQLDatabaseManagerException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new PgSQLDatabaseManagerException("Please add JDBC jar to you project");
        }
        try {
            connection = DriverManager.getConnection(DATABASE_URL + database + LOGGER_LEVEL, userName, password);
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
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
        String sql = String.format("DELETE FROM %s", tableName);
        template.execute(sql);

    }

    @Override
    public void dropTable(String tableName) throws PgSQLDatabaseManagerException {
        String sql = String.format("DROP TABLE %s", tableName);
        template.execute(sql);
    }


    @Override
    public void dropDatabase(String dbName) throws PgSQLDatabaseManagerException {
        try {
            connection = DriverManager.getConnection(DATABASE_URL + LOGGER_LEVEL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            connection = null;
            throw new PgSQLDatabaseManagerException("Can't get connection for database:" + lineSeparator + e.getMessage());
        }

        String checkConnect = String.format("SELECT * FROM pg_stat_activity WHERE datname = '%s'", dbName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkConnect)) {
            String sql = String.format("DROP DATABASE %s", dbName);

            if (resultSet.next()) {
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
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String column : columns) {
                sql.append(column + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(")");

            template.execute(String.valueOf(sql));
    }

    @Override
    public void createDataBase(String dbName) throws PgSQLDatabaseManagerException {
        try {
            connection = DriverManager.getConnection(DATABASE_URL + LOGGER_LEVEL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            connection = null;
            throw new PgSQLDatabaseManagerException("Can't get connection for database:" + lineSeparator + e.getMessage());
        }

        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE DATABASE " + dbName + " WITH ENCODING='UTF8'";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PgSQLDatabaseManagerException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public List<DataSet> findData(String tableName) {
        String sql = String.format("SELECT * FROM %s", tableName);

        List<DataSet> result = template.query(sql,
                new RowMapper<DataSet>() {
                    @Override
                    public DataSet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        DataSet dataSet = new DataSet();
                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                            dataSet.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                        }
                        return dataSet;
                    }
                }
        );
        return result;
    }

    @Override
    public void insertData(String tableName, DataSet input) throws PgSQLDatabaseManagerException {
            String columnNames = collectionToDelimitedString(input.getNames(), ",");
            String values = collectionToDelimitedString(input.getValues(), ",", "'", "'");
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +

                    "VALUES (" + values + ")";

            template.execute(sql);
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) throws PgSQLDatabaseManagerException {
            String columnWhere = collectionToDelimitedString(dataWhere.getNames(), ",");
            String valuesWhere = collectionToDelimitedString(dataWhere.getValues(), ",", "'", "'");
            String columnSet = collectionToDelimitedString(dataSet.getNames(), ",");
            String valuesSet = collectionToDelimitedString(dataSet.getValues(), ",", "'", "'");

            checkRows(tableName, columnWhere, valuesWhere);

            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = " + valuesSet + " " +
                    "WHERE " + columnWhere + " = " + valuesWhere;

            template.execute(sql);
    }

    @Override
    public void deleteRecords(String tableName, DataSet input) throws PgSQLDatabaseManagerException {
        String column = collectionToDelimitedString(input.getNames(), ",");
        String value = collectionToDelimitedString(input.getValues(), ",", "'", "'");

        checkRows(tableName, column, value);

        String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, column, value);

        template.execute(sql);
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
            Set<String> result = new LinkedHashSet<>();

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

}
