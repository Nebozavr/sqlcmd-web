package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class ListTables implements Command {
    public static final String LIST_TABLES_SAMPLE = "list";

    private final View view;
    private final DatabaseManager manager;

    public ListTables(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        try {
            String message = manager.listTables().toString();
            view.write(message);
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
