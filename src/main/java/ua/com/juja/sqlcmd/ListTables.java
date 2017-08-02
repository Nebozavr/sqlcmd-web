package ua.com.juja.sqlcmd;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;

public class ListTables {


    private static final Connection connection = DataBaseManager.getConnection();
    private static final String[] TYPES = {"TABLE"};
    public static String[] result;


    public static void query() {
        try {
            DatabaseMetaData md = connection.getMetaData();
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
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-------------------------------------------");
        System.out.println("Query was executed \n");
    }



}
