package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class DropAllTable implements Command {
    public static final String DROP_ALL_TABLE_SAMPLE = "dropAll";

    private final View view;
    private final DatabaseManager manager;

    public DropAllTable(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("dropAll");
    }

    @Override
    public void process(String command) {
        try {
            manager.dropAllTable();
            view.write(String.format("All tables have been deleted"));
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
