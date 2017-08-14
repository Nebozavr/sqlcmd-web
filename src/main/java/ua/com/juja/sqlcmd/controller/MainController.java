package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{new Exit(view, manager), new Help(view), new List(view, manager),
                new Find(view, manager)};
    }

    public void run() {
        connectToDB();

        while (true) {
            view.write("Enter a new command or use help command.");

            String command = view.read();

            for (int index = 0; index < commands.length; index++) {
                if (commands[index].canProcess(command)) {
                    commands[index].process(command);
                }
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];
        DataSet[] result = manager.findData(tableName);
        String[] tableColumn = manager.getTableColumnsNames(tableName);

        printHeader(tableColumn);
        printTable(result);
    }

    private void printTable(DataSet[] result) {
        for (DataSet row : result) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";

        for (Object value : values) {
            result += value + "|";
        }

        view.write(result);
    }

    private void printHeader(String[] tableData) {
        String result = "|";

        for (String columnName : tableData) {
            result += columnName + "|";
        }

        view.write(result);
    }

    private void connectToDB() {
        view.write("Hello User");
        view.write("Please enter database name, username and password, in the format: database|username|password");
        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3) {
                    throw new IllegalArgumentException("The entered number of parameters is not correct. Must be 3 param, but you enter: " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Connection was successful!");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("Connection was failure because: " + message);
        view.write("Please try again");
    }
}
