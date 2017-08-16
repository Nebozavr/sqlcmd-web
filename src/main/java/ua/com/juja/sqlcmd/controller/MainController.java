package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Exit(view, manager),
                new Help(view),
                new Connect(view, manager),
                new isConnected(view, manager),
                new List(view, manager),
                new Find(view, manager),
                new UnknownCommand(view)};
    }

    public void run() {
        view.write("Hello User");
        view.write("Please enter database name, username and password, in the format: connect|database|username|password");

        try {
            while (true) {

                String input = view.read();

                for (Command command : commands) {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                }
                view.write("Enter a new command or use help command.");
            }
        } catch (ExitException e){
           // e.getMessage();
        }
    }


}
