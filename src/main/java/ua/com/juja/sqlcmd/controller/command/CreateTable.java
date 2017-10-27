package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class CreateTable implements Command {
    public static final String CREATE_TABLE_SAMPLE = "create|tableName|column1Name fieldType,...,columnNName fieldType";

    private final View view;
    private final DatabaseManager manager;

    public CreateTable(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length < 3) {
            throw new WrongNumberParametersException(CREATE_TABLE_SAMPLE ,command);
        }
        String tableName = data[1];
        String columns = data[2];

        try {
            if (manager.hasTable(tableName)) {
                String message = String.format("Table wih name %s is already exists!", tableName);
                view.write(message);
                return;
            }
            manager.createTable(tableName, columns);
            view.write(String.format("Table %s was created!", tableName));
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
