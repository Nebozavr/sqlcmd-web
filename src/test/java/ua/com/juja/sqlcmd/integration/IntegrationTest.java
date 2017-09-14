package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private ConfigurableInputStream in;
    private LogOutputStream out;
    private String lineSeparator = System.getProperty("line.separator");

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "List of all commands:" + lineSeparator +
                "\t connect|database|username|password " + lineSeparator +
                "\t\t Connect to database" + lineSeparator +
                "\t help " + lineSeparator +
                "\t\t View all commands and their description" + lineSeparator +
                "\t list " + lineSeparator +
                "\t\t Show all tables from database" + lineSeparator +
                "\t find|tableName " + lineSeparator +
                "\t\t Show all data from tableName" + lineSeparator +
                "\t create|tableName|column1Name fieldType|...|columnNName fieldType " + lineSeparator +
                "\t\t Create new table" + lineSeparator +
                "\t drop|tableName " + lineSeparator +
                "\t\t Delete table" + lineSeparator +
                "\t clear|tableName " + lineSeparator +
                "\t\t Clear all data from table" + lineSeparator +
                "\t insert|tableName|columnName1|value1|...|columnNameN|valueN " + lineSeparator +
                "\t\t Insert new data to table" + lineSeparator +
                "\t delete|tableName|columnName|value " + lineSeparator +
                "\t\t Delete data from table" + lineSeparator +
                "\t update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet " + lineSeparator +
                "\t\t Update data from table" + lineSeparator +
                "\t exit " + lineSeparator +
                "\t\t Close connection to database and exit program!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testExit() {
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnect() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnectWithWrongNumbersOfParameters() {
        in.add("connect|sqlcmd|yura");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "The entered number of parameters is not correct. Must be 4 param, but you enter: 3" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnectWithError() {
        in.add("connect|sqlcmd|errorName|yura1990");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Can't get connection for database: sqlcmd user: errorName" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUnknownCommand() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("errorCommand");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Unknown command!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testList() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "[invoices, test, test2, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testIsConnected() {
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Before using any command you must connect to database" + lineSeparator +
                "Please connect to database! Use this format: connect|database|username|password" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testCreateTableWitError() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("create|tableName");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like " +
                "\"create|tableName|column1Name fieldType|...|columnNName " + "fieldType\", " +
                "but you enter: create|tableName" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testCreateDropTable() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("create|qwe|testColumn integer");
        in.add("list");
        in.add("drop|qwe");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table qwe was created!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "[invoices, qwe, test, test2, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table qwe was delete" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "[invoices, test, test2, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDropTableWithErrorTableName() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("drop|errorTableName");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Request was not execute, because: ERROR: table \"errortablename\" does not exist" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDropTableWithWrongNumberOfParameters() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("drop|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, " +
                "must be like drop|tableName, but you enter:drop|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testClearTableWithWrongNumberOfParameters() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, in the format: " +
                "connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like clear|tableName, " +
                "but you enter:clear|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testInsertDataWithWrongParameters() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("insert|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like insert|tableName|columnName1|value1|...|columnNameN|valueN, " +
                "but you enter:insert|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testInsertData() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|user_id|10|");
        in.add("clear|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "New data was add to users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDeleteData() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|user_id|10|");
        in.add("insert|users|username|yura22|password|+++++|user_id|12|");
        in.add("find|users");
        in.add("delete|users|username|yura22");
        in.add("find|users");
        in.add("clear|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "New data was add to users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "New data was add to users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "_____________________________" + lineSeparator +
                "| user_id| username| password|" + lineSeparator +
                "|============================|" + lineSeparator +
                "| 10     | yura33  | *****   |" + lineSeparator +
                "| 12     | yura22  | +++++   |" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "The data was delete from table: users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "_____________________________" + lineSeparator +
                "| user_id| username| password|" + lineSeparator +
                "|============================|" + lineSeparator +
                "| 10     | yura33  | *****   |" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDeleteDataWithWrongParameters() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("delete|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like delete|tableName|columnName|value, " +
                "but you enter:delete|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUpdateData() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|user_id|10|");
        in.add("insert|users|username|yura22|password|+++++|user_id|12|");
        in.add("find|users");
        in.add("update|users|username|yura22|password|&&&&&");
        in.add("find|users");
        in.add("clear|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "New data was add to users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "New data was add to users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "_____________________________" + lineSeparator +
                "| user_id| username| password|" + lineSeparator +
                "|============================|" + lineSeparator +
                "| 10     | yura33  | *****   |" + lineSeparator +
                "| 12     | yura22  | +++++   |" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Data from users was updated" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "_____________________________" + lineSeparator +
                "| user_id| username| password|" + lineSeparator +
                "|============================|" + lineSeparator +
                "| 10     | yura33  | *****   |" + lineSeparator +
                "| 12     | yura22  | &&&&&   |" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUpdateDataWithWrongParameters() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("update|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet" +
                ", but you enter:update|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testFind() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|users");
        in.add("find|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "_____________________________" + lineSeparator +
                "| user_id| username| password|" + lineSeparator +
                "|============================|" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was close!" + lineSeparator +
                "Goodbye!!!" + lineSeparator, out.getData());
    }
}
