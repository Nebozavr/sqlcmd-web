package ua.com.juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {


    private  static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void connectionToDataBase(String database, String user, String password) {

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
        System.out.println( "Connection to: " + database + " was successfully \n");
    }
}
