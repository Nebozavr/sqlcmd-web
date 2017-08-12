package ua.com.juja.sqlcmd.model;

import java.sql.*;

public class JDBCDatabaseManager implements DatabaseManager {


    private Connection connection;


    @Override
    public void connect(String database, String userName, String password) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add JDBC jar to you project", e);
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Can't get connection for database:" +
                                                     " %s user: %s", database, userName), e);
        }
    }

    @Override
    public String[] listTables() {
        try {
            DatabaseMetaData md = connection.getMetaData();
            final String[] TYPES = {"TABLE"};
            String[] result;
            ResultSet tables = md.getTables(null, "public", "%", TYPES);

            tables.last();
            result = new String[tables.getRow()];
            tables.beforeFirst();

            int i = 0;
            while (tables.next()) {
                result[i++] = tables.getString(3);
            }

            tables.close();
            return result;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
    }

    @Override
    public void clearTable(String tableName) {

        try {
            Statement statement = connection.createStatement();
            String sql = String.format("DELETE FROM %s", tableName);

            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void dropTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("DROP TABLE %s", tableName);

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void createTable(String tableName, String... columns) {
        try {
            Statement statement = connection.createStatement();
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i] + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(")");

            statement.executeUpdate(String.valueOf(sql));

            statement.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public DataSet[] findData(String tableName) {
        try {
            int size = getSize(tableName);

            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet resultSet = statement.executeQuery(sql);
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

            statement.close();
            resultSet.close();
            return result;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new DataSet[00];
        }
    }

    @Override
    public void insertData(String tableName, DataSet input) {
        try {
            Statement statement = connection.createStatement();

            String columnNames = getNameFormatted(input, "%s,");
            String values = getValuesFormatted(input, "'%s',");
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +
                    "VALUES (" + values + ")";

            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) {
        try {
            Statement statement = connection.createStatement();

            String columnWhere = getNameFormatted(dataWhere, "%s,");
            String valuesWhere = getValuesFormatted(dataWhere, "'%s',");
            String columnSet = getNameFormatted(dataSet, "%s,");
            String valuesSet = getValuesFormatted(dataSet, "'%s',");

            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = " + valuesSet + " " +
                    "WHERE " + columnWhere + " = " + valuesWhere;

            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteRecords(String tableName, DataSet input) {
        try {
            Statement statement = connection.createStatement();

            String column = getNameFormatted(input, "%s,");
            String value = getValuesFormatted(input, "'%s',");

            String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, column, value);
            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void exit() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection, sorry!");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public String[] getTableColumnsNames(String tableName) {
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet tables = md.getColumns(null, null, tableName, null);
            tables.last();
            String[] result = new String[tables.getRow()];
            tables.beforeFirst();

            int i = 0;
            while (tables.next()) {
                result[i++] = tables.getString("column_name");
            }

            tables.close();
            return result;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return new String[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rsCount = statement.executeQuery(String.format("SELECT COUNT(*) FROM %s", tableName));
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    private String getValuesFormatted(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String getNameFormatted(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

}
