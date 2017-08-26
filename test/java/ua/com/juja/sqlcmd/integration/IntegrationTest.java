package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;

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

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "List of all commands:\r\n" +
                "\t connect|database|username|password \r\n" +
                "\t\t Connect to database\r\n" +
                "\t help \r\n" +
                "\t\t View all commands and their description\r\n" +
                "\t list \r\n" +
                "\t\t Show all tables from database\r\n" +
                "\t find|tableName \r\n" +
                "\t\t Show all data from tableName\r\n" +
                "\t create|tableName|column1Name fieldType|...|columnNName fieldType| \r\n" +
                "\t\t Create new table\r\n" +
                "\t drop|tableName \r\n" +
                "\t\t Delete table\r\n" +
                "\t clear|tableName \r\n" +
                "\t\t Clear all data from table\r\n" +
                "\t insert|tableName|columnName1|value1|...|columnNameN|valueN \r\n" +
                        "\t\t Insert new data to table\r\n" +
                "\t exit \r\n" +
                "\t\t Close connection to database and exit program!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }


    @Test
    public void testExit() {

        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testConnect() {

        in.add("connect|sqlcmd|yura|yura1990");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testConnectWithWrongNumbersOfParameters() {

        in.add("connect|sqlcmd|yura");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "An error occurred because: The entered number of parameters is not correct. Must be 4 param, but you enter: 3\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testConnectWithError() {

        in.add("connect|sqlcmd|errorName|yura1990");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "An error occurred because: Can't get connection for database: sqlcmd user: errorName\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testUnknownCommand() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("errorCommand");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Unknown command!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testList() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "[invoices, test, test2, users]\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testIsConnected() {
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Before using any command you must connect to database\r\n" +
                "Please connect to database! Use this format: connect|database|username|password\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }


    @Test
    public void testCreateTableWitError(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("create|tableName");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "An error occurred because: Error entering command, must be like \"create|tableName|column1Name fieldType|...|columnNName fieldType\", but you enter: create|tableName\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testDropTableWithErrorTableName(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("drop|errorTableName");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "An error occurred because: ERROR: table \"errortablename\" does not exist\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testDropTableWithWrongNumberOfParameters(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("drop|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "An error occurred because: Error entering command, must be like drop|tableName, but you enter:drop|errorTableName|test\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testClearTableWithWrongNumberOfParameters(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "An error occurred because: Error entering command, must be like clear|tableName, but you enter:clear|errorTableName|test\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testInsertDataWithWrongParameters(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("insert|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "An error occurred because: Error entering command, must be like insert|tableName|columnName1|value1|...|columnNameN|valueN, but you enter:insert|errorTableName|test\r\n" +
                "Please try again\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testInsertData(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|user_id|10|");
        in.add("clear|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Table users was cleared\r\n" +
                "Enter a new command or use help command.\r\n" +
                "New data was add to users\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Table users was cleared\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());

    }

    @Test
    public void testFind() {
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("find|users");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "|user_id|username|password|\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

}
