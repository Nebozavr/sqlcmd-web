package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Exit implements Command {

    private View view;
    private DatabaseManager manager;

    public Exit(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        manager.exit();
        view.write("Connection was close!");
        view.write("Goodbye!!!");
        System.exit(0);
    }
}
