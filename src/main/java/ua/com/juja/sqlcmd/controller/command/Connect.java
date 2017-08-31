package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {
    public static final String CONNECT_SAMPLE = "connect|database|username|password";

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
        String[] data = command.split("\\|");
        if (data.length != countParams()) {
            throw new IllegalArgumentException(String.format("The entered number of parameters is not correct. " +
                    "Must be %s param, but you enter: %s", countParams(), data.length));
        }
        String databaseName = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connect(databaseName, userName, password);
        view.write("Connection was successful!");

    }

    private int countParams() {
        return CONNECT_SAMPLE.split("\\|").length;
    }
}
