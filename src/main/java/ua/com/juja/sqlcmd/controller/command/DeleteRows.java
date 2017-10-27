package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

public class DeleteRows implements Command {
    public static final String DELETE_ROWS_SAMPLE = "delete|tableName|columnName|value";

    private final View view;
    private final DatabaseManager manager;

    public DeleteRows(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 4) {
            throw new WrongNumberParametersException(DELETE_ROWS_SAMPLE, command);
        }

        String tableName = data[1];
        String columnName = data[2];
        String value = data[3];

        DataSet dataSet = new DataSet();
        dataSet.put(columnName, value);

        try {
            manager.deleteRecords(tableName, dataSet);
            view.write(String.format("The data was delete from table: %s", tableName));
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }
}
