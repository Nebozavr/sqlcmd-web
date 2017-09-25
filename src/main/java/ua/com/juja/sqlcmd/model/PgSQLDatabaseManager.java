package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;

import java.sql.*;
import java.util.*;

public class PgSQLDatabaseManager implements DatabaseManager {

    private Connection connection;
    private String lineSeparator = System.getProperty("line.separator");

    @Override
    public void connect(String database, String userName, String password) throws NoDriverException, BadConnectionException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new NoDriverException("Please add JDBC jar to you project");
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                    database + "?loggerLevel=OFF",  userName, password );
        } catch (SQLException e) {
            connection = null;
            throw new BadConnectionException(String.format("Can't get connection for database:" +
                    " %s ", database) + lineSeparator + e.getMessage());
        }
    }

    @Override
    public Set<String> listTables() throws RequestErrorException {
        String[] types = {"TABLE"};
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }

        try (ResultSet tables = md.getTables(null, "public", "%", types)) {
            Set<String> result = new LinkedHashSet<>();

            while (tables.next()) {
                result.add(tables.getString(3));
            }

            return result;
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void clearTable(String tableName) throws RequestErrorException {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DELETE FROM %s", tableName);

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void dropTable(String tableName) throws RequestErrorException {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DROP TABLE %s", tableName);

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void createTable(String tableName, String... columns) throws RequestErrorException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String column : columns) {
                sql.append(column + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(")");
            statement.executeUpdate(String.valueOf(sql));
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public List<DataSet> findData(String tableName) throws RequestErrorException {
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
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void insertData(String tableName, DataSet input) throws RequestErrorException {
        try (Statement statement = connection.createStatement()) {
            String columnNames = getNameFormatted(input);
            String values = getValuesFormatted(input);
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +
                    "VALUES (" + values + ")";

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) throws RequestErrorException {
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
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public void deleteRecords(String tableName, DataSet input) throws RequestErrorException {
        try (Statement statement = connection.createStatement()) {

            String column = getNameFormatted(input);
            String value = getValuesFormatted(input);

            checkRows(tableName, column, value);

            String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, column, value);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public Set<String> getTableColumnsNames(String tableName) throws RequestErrorException {
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }

        try (ResultSet tables = md.getColumns(null, null, tableName, null)) {
            Set<String> result = new LinkedHashSet<>();

            while (tables.next()) {
                result.add(tables.getString("column_name"));
            }

            return result;
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void checkRows(String tableName, String column, String value) throws RequestErrorException {

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", tableName, column, value);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (!resultSet.next()){
                final String message = String.format("Request was not execute, " +
                        "because the fields with this value (%s = %s) are not in the table %s", column, value, tableName);
                throw new RequestErrorException(message);
            }
        } catch (SQLException e) {
            throw new RequestErrorException("Request was not execute, because: " + e.getMessage());
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
