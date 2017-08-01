package ua.com.juja.sqlcmd;

public class Main {

    public static void main(String[] argv) {

        try {
            ConnectionDB.connectionToDataBase("sqlcmd", "yura", "yura1990");

            Drop.query("orders");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
