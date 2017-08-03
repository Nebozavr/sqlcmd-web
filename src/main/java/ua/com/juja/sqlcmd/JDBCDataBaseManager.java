package ua.com.juja.sqlcmd;

import java.sql.*;

public class JDBCDataBaseManager implements DataBaseManager {


    private Connection connection;


    @Override
    public void connect(String database, String user, String password) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JDBC jar to you project");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("Can't get connection for database: %s user: %s", database, user));
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Connection to: " + database + " was successfully \n");
    }

    @Override
    public String[] listTables() {
        try {
            DatabaseMetaData md = connection.getMetaData();
            String[] TYPES = {"TABLE"};
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
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
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
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
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
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
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
            System.exit(0);
            return new DataSet[00];
        }
    }

    @Override
    public void insertData(String tableName, DataSet input) {
        try {

            Statement statement = connection.createStatement();

            String columnNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" +
                    "VALUES (" + values + ")";


            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    @Override
    public void update(String tableName, DataSet dataWhere, DataSet dataSet) {
        try {
            Statement statement = connection.createStatement();

            String columnWhere = getNameFormated(dataWhere, "%s,");
            String valuesWhere = getValuesFormated(dataWhere, "'%s',");
            String columnSet = getNameFormated(dataSet, "%s,");
            String valuesSet = getValuesFormated(dataSet, "'%s',");

            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = " + valuesSet + " " +
                    "WHERE " + columnWhere + " = " + valuesWhere;

            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    @Override
    public void deleteRecords(String tableName, String column, String value) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("DELETE FROM %s WHERE %s = '%s'", tableName, column, value);
            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    @Override
    public void exit() {
        try {
            connection.close();
            System.out.println("Connection to database was closed \n");
            System.exit(0);

        } catch (SQLException e) {
            System.out.println("Can't close connection, sorry!");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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

    private String getValuesFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

}
