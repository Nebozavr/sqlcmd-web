package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

import java.util.Arrays;

public class Main {

    public static void main(String[] argv) {

        try {
            DatabaseManager databaseManager = new JDBCDatabaseManager();
            databaseManager.connect("sqlcmd", "yura", "yura1990");
           // System.out.println(Arrays.toString(databaseManager.findData("users")));
          //  databaseManager.dropTable("testtable");
          //  System.out.println(Arrays.toString(databaseManager.listTables()));
            DataSet where = new DataSet();
            where.put("username", "yura2");
//            DataSet set = new DataSet();
//            set.put("password", "ahaha1990");

            //databaseManager.insertData("users", input);

           System.out.println(Arrays.toString(databaseManager.listTables()));

           databaseManager.deleteRecords("users", where);

            System.out.println(Arrays.toString(databaseManager.listTables()));


            databaseManager.exit();




        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
