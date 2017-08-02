package ua.com.juja.sqlcmd;

public class Main {

    public static void main(String[] argv) {

        try {
            DataBaseManager dataBaseManager = new DataBaseManager();
            dataBaseManager.connect("sqlcmd", "yura", "yura1990");
            dataBaseManager.findData("users");




        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
