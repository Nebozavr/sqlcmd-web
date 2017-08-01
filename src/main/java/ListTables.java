import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class ListTables {


    private static final Connection connection = ConnectionDB.getConnection();
    private static final String[] TYPES = {"TABLE"};


    public static void query() {
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet tables = md.getTables(null, "public", "%", TYPES);
            while (tables.next()) {
                System.out.println(tables.getString(3));
            }


            tables.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }


}
