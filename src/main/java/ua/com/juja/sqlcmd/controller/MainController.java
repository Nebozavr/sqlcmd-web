package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private final Command[] commands;
    private final View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Exit(view, manager),
                new Help(view),
                new Connect(view, manager),
                new Disconnect(view, manager),
                new isConnected(view, manager),
                new DropDatabase(view, manager),
                new CreateDatabase(view, manager),
                new ListTables(view, manager),
                new FindData(view, manager),
                new CreateTable(view, manager),
                new DropTable(view, manager),
                new DropAllTable(view, manager),
                new ClearTable(view, manager),
                new InsertData(view, manager),
                new DeleteRows(view, manager),
                new UpdateData(view, manager),
                new UnknownCommand(view)};
    }

    public void run() {
        view.write("Hello User");
        view.write("Please enter database name, username and password, " +
                "in the format: " + Connect.CONNECT_SAMPLE);

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (WrongNumberParametersException e) {
                    view.writeError(e);
                    break;
                }
            }

            if (input.equals(Exit.EXIT_SAMPLE)){
                break;
            }
            view.write("Enter a new command or use help command.");
        }
    }
}
