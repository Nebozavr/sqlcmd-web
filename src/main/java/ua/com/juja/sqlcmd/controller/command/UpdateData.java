package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class UpdateData implements Command {
    private View view;
    private DatabaseManager manager;

    public UpdateData(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 6){
            throw new IllegalArgumentException("Error entering command, must be like " +
                    "update|tableName|columnNameSet|valueSet|columnNameWhere|valueWhere, but you enter:" + command);
        }

        String tableName = data[1];
        String columnWhere = data[2];
        String valuesWhere = data[3];
        String columnSet = data[4];
        String valuesSet = data[5];

        DataSet dataSet = new DataSet();
        dataSet.put(columnSet, valuesSet);

        DataSet dataWhere = new DataSet();
        dataWhere.put(columnWhere, valuesWhere);

        manager.update(tableName, dataWhere, dataSet);
        view.write(String.format("Data from %s was updated", tableName));
    }
}
