import java.sql.Connection;
import java.sql.Statement;

public class Delete {


    private static Statement statement = null;
    private static final Connection connection = ConnectionDB.getConnection();


    public static void query(String tableName, String column, String value) {
        try {
            statement = connection.createStatement();
            String sql = "DELETE FROM " + tableName + " " +
                         "WHERE " + column + " = '" + value + "'";
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
