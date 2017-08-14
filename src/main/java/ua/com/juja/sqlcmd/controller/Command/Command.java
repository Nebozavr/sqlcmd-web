package ua.com.juja.sqlcmd.controller.Command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);
}
