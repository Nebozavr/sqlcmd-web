package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Create implements Command {
    private View view;
    private DatabaseManager manager;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length < 3){
            throw new IllegalArgumentException("Error entering command, must be like " +
                    "\"create|tableName|column1Name fieldType|...|columnNName fieldType\", but you enter: " + command);
        }
        String tableName = data[1];
        String columns = data[2];

        manager.createTable(tableName, columns);

        view.write(String.format("Table %s was created!", tableName));
    }
}
