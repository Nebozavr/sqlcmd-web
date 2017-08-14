package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[] {new Exit(view, manager),
                                       new Help(view),
                                       new List(view, manager),
                                       new Find(view, manager),
                                       new UnknownCommand(view)};
    }

    public void run() {
        connectToDB();

        while (true) {
            view.write("Enter a new command or use help command.");

            String input = view.read();

            for (Command command: commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
        }
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
