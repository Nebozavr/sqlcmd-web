package ua.com.juja.sqlcmd.controller.command;

import dnl.utils.text.table.TextTable;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.view.View;

public class FindData implements Command {
    public static final String FIND_DATA_SAMPLE = "find|tableName";

    private DatabaseManager manager;
    private View view;

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
        } catch (RequestErrorException e) {
            view.writeError(e);
        }
    }

    private void printTable(String tableName) throws RequestErrorException {
        String[] tableColumn = manager.getTableColumnsNames(tableName);

        DataSet[] result = manager.findData(tableName);

        Object[][] values = new Object[result.length][tableColumn.length];

        for (int i = 0; i < result.length; i++) {
            values[i] = result[i].getValues();
        }

        TextTable table = new TextTable(tableColumn, values);

        table.printTable();
    }
}
