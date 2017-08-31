package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class DeleteRows implements Command {
    private View view;
    private DatabaseManager manager;

    public DeleteRows(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 4){
            throw new IllegalArgumentException("Error entering command, must be like " +
                    "delete|tableName|columnName|value, but you enter:" + command);
        }

        String tableName = data[1];
        String columnName = data[2];
        String value = data[3];

        DataSet dataSet = new DataSet();
        dataSet.put(columnName, value);

        manager.deleteRecords(tableName, dataSet);
        view.write(String.format("The data was delete from table: %s", tableName));
    }
}
