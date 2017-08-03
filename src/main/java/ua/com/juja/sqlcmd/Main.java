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
            DataSet input = new DataSet();
            input.put("username", "yura2");
            input.put("password", "yurapassword2");

            dataBaseManager.insertData("users", input);




        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
