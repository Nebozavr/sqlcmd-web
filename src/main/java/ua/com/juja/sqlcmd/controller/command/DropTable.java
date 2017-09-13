package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.view.View;

public class DropTable implements Command {
    public static final String DROP_TABLE_SAMPLE = "drop|tableName";

    private View view;
    private DatabaseManager manager;

    public DropTable(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Error entering command, must be like " +
                    DROP_TABLE_SAMPLE + ", but you enter:" + command);
        }

        String tableName = data[1];

        try {
            manager.dropTable(tableName);
            view.write(String.format("Table %s was delete", tableName));
        } catch (RequestErrorException e) {
            view.writeError(e);
        }

    }
}
