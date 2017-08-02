package ua.com.juja.sqlcmd;

import java.sql.*;

public class DataBaseManager {


    private Connection connection;
    private static String[] result;

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
            e.printStackTrace();
        }
        System.out.println("Connection to: " + database + " was successfully \n");
    }

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

    public void deleteRecords(String tableName, String column, String value) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("DELETE FROM %s WHERE %s = '%s'",tableName, column, value);
            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    public void dropTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("DROP TABLE %s",tableName);

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    public void findData(String tableName) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    public void insertData(String tableName, String... values) {
        try {

            Statement statement = connection.createStatement();
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");

            for (int i = 0; i < values.length; i += 2) {
                sql.append(values[i] + ",");
            }

            sql.delete(sql.length() - 1, sql.length()).append(") VALUES (");


            for (int i = 1; i < values.length; i += 2) {
                sql.append("'" + values[i] + "'" + ",");
            }
            sql.delete(sql.length() - 1, sql.length()).append(");");


            statement.executeUpdate(String.valueOf(sql));

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    public void listTables() {
        try {
            DatabaseMetaData md = connection.getMetaData();
            String[] TYPES = {"TABLE"};
            ResultSet tables = md.getTables(null, "public", "%", TYPES);

            tables.last();
            result = new String[tables.getRow()];
            int i = 0;
            tables.beforeFirst();

            while (tables.next()) {
                result[i++] = tables.getString(3);
                System.out.println(tables.getString(3));
            }

            tables.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }

    public void update(String tableName, String columnWhere, String valueWhere, String columnSet, String valueSet) {
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE " + tableName + " " +
                    "SET " + columnSet + " = '" + valueSet + "' " +
                    "WHERE " + columnWhere + " = '" + valueWhere + "'";
            statement.executeUpdate(sql);

            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }


}
