package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class isConnected implements Command {
    private View view;
    private DatabaseManager manager;

    public isConnected(View view, DatabaseManager manager) {

        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write("Before using any command you must connect to database");
        view.write("Please connect to database! Use this format: connect|database|username|password");
    }
}
