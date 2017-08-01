import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static String database = "sqlcmd";
    private static String user = "yura";
    private static String password = "yura1990";
    private  static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void connectionToDataBase() throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, password);
        System.out.println( "Connection to: " + database + " was successfully \n");
    }
}
