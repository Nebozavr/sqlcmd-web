package ua.com.juja.sqlcmd.controller.command;

import dnl.utils.text.table.TextTable;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;

public class FindData implements Command {
    public static final String  FIND_DATA_SAMPLE = "find|tableName";

    private DatabaseManager manager;

    public FindData(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];

        printTable(tableName);
    }

    private void printTable(String tableName) {
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
