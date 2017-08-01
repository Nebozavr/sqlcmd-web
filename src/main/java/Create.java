import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class Create {


    private static final Connection connection = ConnectionDB.getConnection();
    private static Statement statement = null;

    public static void query(String tableName, String... columns) {
        try {

            statement = connection.createStatement();
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i] + ",");
            }

            sql.delete(sql.length() - 1, sql.length());
            sql.append(")");

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
