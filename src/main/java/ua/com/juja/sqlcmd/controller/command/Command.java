package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;

public interface Command {

    boolean canProcess(String command);

    void process(String command) throws WrongNumberParametersException;
}
