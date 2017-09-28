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
            printTable(tableName);
        } catch (PgSQLDatabaseManagerException e) {
            view.writeError(e);
        }
    }

    private void printTable(String tableName) throws PgSQLDatabaseManagerException {
        String[] tableColumn = manager.getTableColumnsNames(tableName).toArray(new String[0]);

        List<DataSet> result = manager.findData(tableName);

        Object[][] values = new Object[result.size()][tableColumn.length];

        for (int i = 0; i < result.size(); i++) {
            values[i] = result.get(i).getValues().toArray();
        }

        TextTable table = new TextTable(tableColumn, values);

        table.printTable();
    }
}
