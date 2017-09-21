package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class ListTables implements Command {
    public static final String LIST_TABLES_SAMPLE = "list";

    private View view;
    private DatabaseManager manager;

    public ListTables(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        try {
            String tablesNames = Arrays.toString(manager.listTables());
            view.write(tablesNames);
        } catch (RequestErrorException e) {
            view.writeError(e);
        }
    }
}
