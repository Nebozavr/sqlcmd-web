package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PgSQLDatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class Main {

    public static void main(String[] argv) {
        DatabaseManager manager = new PgSQLDatabaseManager();
        View view = new Console();

        MainController mainController = new MainController(view, manager);
        mainController.run();
    }
}
