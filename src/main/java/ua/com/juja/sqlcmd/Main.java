package ua.com.juja.sqlcmd;

public class Main {

    public static void main(String[] argv) {

        try {
            Connect.connectionToDataBase("sqlcmd", "yura", "yura1990");

            ListTables.query();


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
