package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.ExitException;
import ua.com.juja.sqlcmd.view.View;

public class Exit implements Command {
    public static final String EXIT_SAMPLE = "exit";

    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("Connection was closed!");
        view.write("Bye!!!");
        throw new ExitException();
    }
}
