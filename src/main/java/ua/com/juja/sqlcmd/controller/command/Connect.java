package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {

    private View view;
    private DatabaseManager manager;

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
          try {

            String[] data = command.split("\\|");
            if (data.length != 4) {
                throw new IllegalArgumentException("The entered number of parameters is not correct. Must be 4 param, but you enter: " + data.length);
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];

            manager.connect(databaseName, userName, password);
            view.write("Connection was successful!");
        } catch (Exception e) {
            printError(e);
        }
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
