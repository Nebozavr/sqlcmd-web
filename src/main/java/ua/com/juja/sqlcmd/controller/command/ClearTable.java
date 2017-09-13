package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.view.View;

public class ClearTable implements Command {
    private View view;
    private DatabaseManager manager;

    public static final String CLEAR_TABLE_SAMPLE = "clear|tableName";

    public ClearTable(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new WrongNumberParametersException("Error entering command, must be like " +
                    CLEAR_TABLE_SAMPLE + ", but you enter:" + command);
        }
        String tableName = data[1];

        try {
            manager.clearTable(tableName);
            view.write(String.format("Table %s was cleared", tableName));
        } catch (RequestErrorException e) {
            view.writeError(e);
        }

    }
}
