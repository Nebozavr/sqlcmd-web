package ua.com.juja.sqlcmd.controller.command;

import dnl.utils.text.table.TextTable;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;

public class FindData implements Command {
    public static final String FIND_DATA_SAMPLE = "find|tableName";

    private final DatabaseManager manager;
    private final View view;

    public FindData(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new WrongNumberParametersException("Error entering command, must be like " +
                    FIND_DATA_SAMPLE + ", but you enter:" + command);
        }
        String tableName = data[1];

        try {
            if (!manager.hasTable(tableName)) {
                String message = String.format("Table %s doesn't exists!", tableName);
                view.write(message);
                return;
            }
            view.writeTable(tableName, manager);
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }


}
