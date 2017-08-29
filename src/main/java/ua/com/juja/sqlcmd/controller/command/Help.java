package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command {

    private View view;

    private String lineSeparator = System.getProperty("line.separator");

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {

        view.write("List of all commands:");
        view.write("\t connect|database|username|password "+ lineSeparator +
                        "\t\t Connect to database");
        view.write("\t help "+ lineSeparator +
                        "\t\t View all commands and their description");
        view.write("\t list "+ lineSeparator +
                        "\t\t Show all tables from database");
        view.write("\t find|tableName "+ lineSeparator +
                        "\t\t Show all data from tableName");
        view.write("\t create|tableName|column1Name fieldType|...|columnNName fieldType| "+ lineSeparator +
                        "\t\t Create new table");
        view.write("\t drop|tableName "+ lineSeparator +
                        "\t\t Delete table");
        view.write("\t clear|tableName "+ lineSeparator +
                        "\t\t Clear all data from table");
        view.write("\t insert|tableName|columnName1|value1|...|columnNameN|valueN "+ lineSeparator +
                        "\t\t Insert new data to table");
        view.write("\t delete|tableName|columnName|value "+ lineSeparator +
                        "\t\t Delete data from table");
        view.write("\t update|tableName|columnNameSet|valueSet|columnNameWhere|valueWhere "+ lineSeparator +
                        "\t\t Update data from table");
        view.write("\t exit "+ lineSeparator +
                        "\t\t Close connection to database and exit program!");
    }
}
