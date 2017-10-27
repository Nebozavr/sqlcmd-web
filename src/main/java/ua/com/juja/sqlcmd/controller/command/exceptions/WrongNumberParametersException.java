package ua.com.juja.sqlcmd.controller.command.exceptions;

public class WrongNumberParametersException extends Exception {

    public WrongNumberParametersException(String commandSample, String command) {
        super("Error entering command, must be like " +
                commandSample + ", but you enter: " + command);
    }
}
