package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class DropDatabase implements Command {
    public static final String DROP_DB_SAMPLE = "dropDB|databaseName";

    private View view;
    private DatabaseManager manager;

    public DropDatabase(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropDB|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new WrongNumberParametersException("Error entering command, must be like " +
                    DROP_DB_SAMPLE + ", but you enter:" + command);
        }

        String dbName = data[1];

        try {
            manager.dropDatabase(dbName);
            view.write(String.format("Database %s was delete", dbName));
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
