package ua.com.juja.sqlcmd.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PgSQLDatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.utils.PropertiesLoader;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String DATABASE = propertiesLoader.getDatabaseName();
    private static final String USER = propertiesLoader.getUserName();
    private static final String PASSWORD = propertiesLoader.getPassword();

    private ConfigurableInputStream in;
    private LogOutputStream out;
    private final String lineSeparator = System.getProperty("line.separator");
    private DatabaseManager databaseManager;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        try {
            databaseManager = new PgSQLDatabaseManager();
            databaseManager.connect(DATABASE, USER, PASSWORD);

            databaseManager.createTable("users", "id int", "userName text", "password text");
            databaseManager.createTable("roles", "roleID int", "roleName text", "description text");

        } catch (BadConnectionException | NoDriverException | RequestErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void after() {
        try {
            databaseManager.dropTable("users");
            databaseManager.dropTable("roles");
        } catch (RequestErrorException e) {
            e.getMessage();
        }
    }

    @Test
    public void testHelp() {
        in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, in the format: connect|database|username|password" + lineSeparator +
                "List of all commands:" + lineSeparator + lineSeparator +
                "\t connect|database|username|password " + lineSeparator +
                "\t\t Connect to database" + lineSeparator + lineSeparator +
                "\t help " + lineSeparator +
                "\t\t View all commands and their description" + lineSeparator + lineSeparator +
                "\t list " + lineSeparator +
                "\t\t Show all tables from database" + lineSeparator + lineSeparator +
                "\t find|tableName " + lineSeparator +
                "\t\t Show all data from tableName" + lineSeparator + lineSeparator +
                "\t create|tableName|column1Name fieldType|...|columnNName fieldType " + lineSeparator +
                "\t\t Create new table" + lineSeparator + lineSeparator +
                "\t drop|tableName " + lineSeparator +
                "\t\t Delete table" + lineSeparator + lineSeparator +
                "\t clear|tableName " + lineSeparator +
                "\t\t Clear all data from table" + lineSeparator + lineSeparator +
                "\t insert|tableName|columnName1|value1|...|columnNameN|valueN " + lineSeparator +
                "\t\t Insert new data to table" + lineSeparator + lineSeparator +
                "\t delete|tableName|columnName|value " + lineSeparator +
                "\t\t Delete data from table" + lineSeparator + lineSeparator +
                "\t update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet " + lineSeparator +
                "\t\t Update data from table" + lineSeparator + lineSeparator +
                "\t exit " + lineSeparator +
                "\t\t Close connection to database and exit program!" + lineSeparator + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testExit() {
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnect() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnectWithWrongNumbersOfParameters() {
        in.add("connect|" + DATABASE + "|" + USER);
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "The entered number of parameters is not correct. Must be 4 param, but you enter: 3" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testConnectWithError() {
        in.add("connect|" + DATABASE + "|errorUser|" + PASSWORD);
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Can't get connection for database: sqlcmd " + lineSeparator +
                "FATAL: password authentication failed for user \"errorUser\"" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUnknownCommand() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testList() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, " +
                "in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "[roles, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testCreateTableWitError() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testCreateDropTable() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "[qwe, roles, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Table qwe was delete" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "[roles, users]" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDropTableWithErrorTableName() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDropTableWithWrongNumberOfParameters() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testClearTableWithWrongNumberOfParameters() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testInsertDataWithWrongParameters() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testInsertData() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|id|10|");
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDeleteData() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|id|10|");
        in.add("insert|users|username|yura22|password|+++++|id|12|");
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
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "The data was delete from table: users" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDeleteDataWithWrongParameters() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testDeleteDataForEmptyRows() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|id|10|");
        in.add("insert|users|username|yura22|password|+++++|id|12|");
        in.add("find|users");
        in.add("delete|users|username|errorValue");
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
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Request was not execute, because the fields with this value " +
                "(username = 'errorValue') are not in the table users" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUpdateData() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|id|10|");
        in.add("insert|users|username|yura22|password|+++++|id|12|");
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
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Data from users was updated" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | &&&&&   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUpdateDataForEmptyRows() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("clear|users");
        in.add("insert|users|username|yura33|password|*****|id|10|");
        in.add("insert|users|username|yura22|password|+++++|id|12|");
        in.add("find|users");
        in.add("update|users|username|errorValue|password|&&&&&");
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
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Request was not execute, because the fields with this value " +
                "(username = 'errorValue') are not in the table users" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "| 10| yura33  | *****   |\n" +
                "| 12| yura22  | +++++   |\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Table users was cleared" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testUpdateDataWithWrongParameters() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
        in.add("update|errorTableName|test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User" + lineSeparator +
                "Please enter database name, username and password, in the format: connect|database|username|password" + lineSeparator +
                "Connection was successful!" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Error entering command, must be like " +
                "update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet, but you enter:update|errorTableName|test" + lineSeparator +
                "Please try again" + lineSeparator +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator + "", out.getData());
    }

    @Test
    public void testFind() {
        in.add("connect|" + DATABASE + "|" + USER + "|" + PASSWORD);
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
                "________________________\r\n" +
                "| id| username| password|\n" +
                "|=======================|\r\n" +
                "Enter a new command or use help command." + lineSeparator +
                "Connection was closed!" + lineSeparator +
                "Bye!!!" + lineSeparator, out.getData());
    }
}
