package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Find implements Command {

    private View view;
    private DatabaseManager manager;

    public Find(View view, DatabaseManager manager) {
        this.view = view;
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

        String[] tableColumn = manager.getTableColumnsNames(tableName);
        printHeader(tableColumn);

        DataSet[] result = manager.findData(tableName);
        printTable(result);
    }

    private void printTable(DataSet[] result) {
        for (DataSet row : result) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";

        for (Object value : values) {
            result += value + "|";
        }

        view.write(result);
    }

    private void printHeader(String[] tableData) {
        String result = "|";

        for (String columnName : tableData) {
            result += columnName + "|";
        }

        view.write(result);
    }
}
