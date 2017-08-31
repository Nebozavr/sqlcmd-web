package ua.com.juja.sqlcmd.model;

import java.sql.*;

public class PgSQLDatabaseManager implements DatabaseManager {


    private Connection connection;


    @Override
    public void connect(String database, String userName, String password) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add JDBC jar to you project", e);
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                                                        database, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Can't get connection for database:" +
                    " %s user: %s", database, userName), e);
        }
    }

    @Override
    public String[] listTables() {
        String[] types = {"TABLE"};
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
        try (ResultSet tables = md.getTables(null, "public", "%", types)) {

            tables.last();
            String[] result = new String[tables.getRow()];
            tables.beforeFirst();

            int i = 0;
            while (tables.next()) {
                result[i++] = tables.getString(3);
            }

            return result;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
    }

    @Override
    public void clearTable(String tableName) {

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DELETE FROM %s", tableName);

            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void dropTable(String tableName) {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("DROP TABLE %s", tableName);

            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void createTable(String tableName, String... columns) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String column : columns) {
                sql.append(column + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(")");

            statement.executeUpdate(String.valueOf(sql));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public DataSet[] findData(String tableName) {
        int size = getSize(tableName);
        String sql = String.format("SELECT * FROM %s", tableName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            resultSetMetaData.getColumnName(1);
            DataSet[] result = new DataSet[size];
            int index = 0;

            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    dataSet.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
            }

            return result;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new DataSet[0];
        }
    }

    @Override
    public void insertData(String tableName, DataSet input) {
        try (Statement statement = connection.createStatement()) {
            String columnNames = getNameFormatted(input);
            String values = getValuesFormatted(input);
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +
                    "VALUES (" + values + ")";

            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) {
        try (Statement statement = connection.createStatement()) {

            String columnWhere = getNameFormatted(dataWhere);
            String valuesWhere = getValuesFormatted(dataWhere);
            String columnSet = getNameFormatted(dataSet);
            String valuesSet = getValuesFormatted(dataSet);

            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = " + valuesSet + " " +
                    "WHERE " + columnWhere + " = " + valuesWhere;

            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteRecords(String tableName, DataSet input) {
        try (Statement statement = connection.createStatement()) {

            String column = getNameFormatted(input);
            String value = getValuesFormatted(input);

            String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, column, value);
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String[] getTableColumnsNames(String tableName) {
        DatabaseMetaData md;
        try {
            md = connection.getMetaData();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
        try(ResultSet tables = md.getColumns(null, null, tableName, null)) {

            tables.last();
            String[] result = new String[tables.getRow()];
            tables.beforeFirst();

            int i = 0;
            while (tables.next()) {
                result[i++] = tables.getString("column_name");
            }

            return result;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private int getSize(String tableName) {
        String format = String.format("SELECT COUNT(*) FROM %s", tableName);

        try (Statement statement = connection.createStatement();
             ResultSet rsCount = statement.executeQuery(format)) {
            rsCount.next();
            return rsCount.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
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
