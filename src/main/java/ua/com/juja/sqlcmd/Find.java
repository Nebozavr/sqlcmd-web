package ua.com.juja.sqlcmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Find {


    private static Statement statement = null;
    private static final Connection connection = DataBaseManager.getConnection();


    public static void query(String tableName) {
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }

            statement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }


}
