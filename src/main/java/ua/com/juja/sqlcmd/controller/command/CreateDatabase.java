package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class CreateDatabase implements Command {
    public static final String CREATE_DB_SAMPLE = "createDB|databaseName";

    private final View view;
    private final DatabaseManager manager;

    public CreateDatabase(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDB|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new WrongNumberParametersException("Error entering command, must be like " +
                    CREATE_DB_SAMPLE + ", but you enter:" + command);
        }
        String dbName = data[1];

        try {
            manager.createDataBase(dbName);
            view.write(String.format("Database %s was created", dbName));
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
