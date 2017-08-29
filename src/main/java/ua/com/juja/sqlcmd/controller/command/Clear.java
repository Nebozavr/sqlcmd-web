package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Clear implements Command {
    private View view;
    private DatabaseManager manager;

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return  command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2){
            throw new IllegalArgumentException("Error entering command, " +
                    "must be like clear|tableName, but you enter:" + command);
        }
        String tableName = data[1];

        manager.clearTable(tableName);

        view.write(String.format("Table %s was cleared", tableName));
    }
}
