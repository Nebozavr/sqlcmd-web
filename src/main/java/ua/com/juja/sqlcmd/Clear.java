package ua.com.juja.sqlcmd;

import java.sql.Connection;
import java.sql.Statement;

public class Clear {


    private static Statement statement = null;
    private static final Connection connection = Connect.getConnection();


    public static void query(String tableName) {
        try {
            statement = connection.createStatement();
            String sql = "DELETE FROM " + tableName;
            statement.executeUpdate(sql);
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
