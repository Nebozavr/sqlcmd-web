package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command {
    private static final String HELP_SAMPLE = "help";

    private final View view;

    private final String lineSeparator = System.getProperty("line.separator");


    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("List of all commands:" + lineSeparator);
        view.write("\t " + Connect.CONNECT_SAMPLE + " " + lineSeparator +
                "\t\t Connect to database" + lineSeparator);
        view.write("\t " + Disconnect.DISCONNECT_SAMPLE + " " + lineSeparator +
                "\t\t Disconnect from current database" + lineSeparator);
        view.write("\t " + HELP_SAMPLE + " " + lineSeparator +
                "\t\t View all commands and their description" + lineSeparator);
        view.write("\t " + ListTables.LIST_TABLES_SAMPLE + " " + lineSeparator +
                "\t\t Show all tables from database" + lineSeparator);
        view.write("\t " + FindData.FIND_DATA_SAMPLE + " " + lineSeparator +
                "\t\t Show all data from tableName" + lineSeparator);
        view.write("\t " + CreateTable.CREATE_TABLE_SAMPLE + " " + lineSeparator +
                "\t\t Create new table" + lineSeparator);
        view.write("\t " + DropTable.DROP_TABLE_SAMPLE + " " + lineSeparator +
                "\t\t Delete table" + lineSeparator);
        view.write("\t " + DropAllTable.DROP_ALL_TABLE_SAMPLE + " " + lineSeparator +
                "\t\t Delete all tables" + lineSeparator);
        view.write("\t " + ClearTable.CLEAR_TABLE_SAMPLE + " " + lineSeparator +
                "\t\t Clear all data from table" + lineSeparator);
        view.write("\t " + InsertData.INSERT_DATA_SAMPLE + " " + lineSeparator +
                "\t\t Insert new data to table" + lineSeparator);
        view.write("\t " + DeleteRows.DELETE_ROWS_SAMPLE + " " + lineSeparator +
                "\t\t Delete data from table" + lineSeparator);
        view.write("\t " + UpdateData.UPDATE_DATA_SAMPLE + " " + lineSeparator +
                "\t\t Update data from table" + lineSeparator);
        view.write("\t " + Exit.EXIT_SAMPLE + " " + lineSeparator +
                "\t\t Close connection to database and exit program!" + lineSeparator);
    }
}
