package ua.com.juja.sqlcmd;

import java.util.Arrays;

public class Main {

    public static void main(String[] argv) {

        try {
            DataBaseManager dataBaseManager = new JDBCDataBaseManager();
            dataBaseManager.connect("sqlcmd", "yura", "yura1990");
           // System.out.println(Arrays.toString(dataBaseManager.findData("users")));
          //  dataBaseManager.dropTable("testtable");
          //  System.out.println(Arrays.toString(dataBaseManager.listTables()));
//            DataSet where = new DataSet();
//            where.put("username", "yura2");
//            DataSet set = new DataSet();
//            set.put("password", "ahaha1990");

            //dataBaseManager.insertData("users", input);

           System.out.println(Arrays.toString(dataBaseManager.listTables()));

            dataBaseManager.exit();




        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
