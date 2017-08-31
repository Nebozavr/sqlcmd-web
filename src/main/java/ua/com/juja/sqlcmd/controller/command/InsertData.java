package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class InsertData implements Command {
    public static final String INSERT_DATA_SAMPLE = "insert|tableName|columnName1|value1|...|columnNameN|valueN";

    private View view;
    private DatabaseManager manager;

    public InsertData(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Error entering command, must be like " +
                    INSERT_DATA_SAMPLE + ", but you enter:" + command);
        }

        String tableName = data[1];

        DataSet dataSet = new DataSet();
        for (int index = 1; index < data.length / 2; index++) {
            String columnName = data[index * 2];
            String value = data[index * 2 + 1];

            dataSet.put(columnName, value);
        }

        manager.insertData(tableName, dataSet);
        view.write(String.format("New data was add to %s", tableName));
    }
}
