package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class Exit implements Command {
    public static final String EXIT_SAMPLE = "exit";

    private final View view;
    private DatabaseManager manager;

    public Exit(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        if (manager.isConnected()){
            try {
                manager.disconnect();
                view.write("Connection was closed!");
            } catch (PgSQLDatabaseManagerException e) {
                view.writeError(e);
            }
        }
        view.write("Bye!!!");
    }
}
