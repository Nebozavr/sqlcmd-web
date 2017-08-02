package ua.com.juja.sqlcmd;

import java.sql.Connection;
import java.sql.Statement;

public class Insert {


    private static Statement statement = null;
    private static final Connection connection = DataBaseManager.getConnection();


    public static void query(String tableName, String... values) {
        try {

            statement = connection.createStatement();
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");

            for (int i = 0; i < values.length; i += 2) {
                sql.append(values[i] + ",");
            }

            sql.delete(sql.length() - 1, sql.length());
            sql.append(") VALUES (");

            for (int i = 1; i < values.length; i += 2) {
                sql.append("'" + values[i] + "'" + ",");
            }
            sql.delete(sql.length() - 1, sql.length());
            sql.append(");");

            statement.executeUpdate(String.valueOf(sql));

            statement.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }


}
