package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class Disconnect implements Command {
    public static final String DISCONNECT_SAMPLE = "disconnect";

    private View view;
    private DatabaseManager manager;

    public Disconnect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("disconnect");
    }

    @Override
    public void process(String command) {
        try {
            manager.disconnect();
            view.write("Connection was closed");
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
