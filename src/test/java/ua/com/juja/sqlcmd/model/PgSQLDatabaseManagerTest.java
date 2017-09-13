package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PgSQLDatabaseManagerTest {

    private DatabaseManager databaseManager;

    @Before
    public void setup() {
        try {
            databaseManager = new PgSQLDatabaseManager();
            databaseManager.connect("sqlcmd", "yura", "yura1990");   //Test for connection

        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
    }

    //Test for listTables

    @Test
    public void connectionWithErrorTest() {
        String error = "";
        try {
            DatabaseManager testManager = new PgSQLDatabaseManager();
            testManager.connect("sqlcmd", "yura", "errorPass");
        } catch (NoDriverException | BadConnectionException e) {
            error += e.getMessage();
        }
        assertEquals(error, "Can't get connection for database: sqlcmd user: yura");
    }

    @Test
    public void listTableTest() throws RequestErrorException {
        String[] result = new String[]{"invoices", "test", "test2", "users"};

        assertArrayEquals(databaseManager.listTables(), result);
    }

    //Test for dropTable
    @Test
    public void dropTableTest() throws RequestErrorException {
        databaseManager.dropTable("test");

        String[] result = new String[]{"invoices", "test2", "users"};
        assertArrayEquals(databaseManager.listTables(), result);

    }

    @Test
    public void dropTableWithErrorTest() {
        String error = "";
        try {
            databaseManager.dropTable("errorTableName");
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: table \"errortablename\" does not exist");
    }


    //Test for createTable
    @Test
    public void createTableTest() throws RequestErrorException {
        databaseManager.createTable("test", "id integer", "name text");


        String[] result = new String[]{"invoices", "test", "test2", "users"};
        assertArrayEquals(databaseManager.listTables(), result);

    }

    @Test
    public void createTableWithErrorTest() {
        String error = "";
        try {
            databaseManager.createTable("test", "id integer", "name text");
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: relation \"test\" already exists");
    }


    //Test for findData
    @Test
    public void findDataTest() throws RequestErrorException {
        databaseManager.clearTable("users");   //Test for clearTable

        DataSet input = new DataSet();
        input.put("username", "yura22");
        input.put("user_id", "1");
        input.put("password", "qwerty");

        databaseManager.insertData("users", input); //Test for insertTable

        DataSet[] users = databaseManager.findData("users");


        DataSet user = users[0];
        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[1, yura22, qwerty]", Arrays.toString(user.getValues()));

        databaseManager.clearTable("users");
    }

    @Test
    public void findDataWithError() {

        String error = "";
        try {
            databaseManager.findData("errorTableName");
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: relation \"errortablename\" does not exist\n" +
                "  Position: 22");
    }

    @Test
    public void insertDataWithError() {
        String error = "";
        try {
            DataSet input = new DataSet();
            input.put("username", "yura22");
            input.put("user_id", "1");
            input.put("password", "qwerty");
            databaseManager.insertData("errorTableName", input);
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: relation \"errortablename\" does not exist\n" +
                "  Position: 13");
    }

    //Test for updateData
    @Test
    public void updateTest() throws RequestErrorException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yuraTest");
        input.put("user_id", "2");
        input.put("password", "passwordTest");

        databaseManager.insertData("users", input);

        DataSet where = new DataSet();
        where.put("username", "yuraTest");

        DataSet output = new DataSet();
        output.put("password", "changePass");

        databaseManager.update("users", where, output);

        DataSet[] users = databaseManager.findData("users");


        DataSet user = users[0];
        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[2, yuraTest, changePass]", Arrays.toString(user.getValues()));
    }

    @Test
    public void updateDataWithError() {
        String error = "";
        try {
            DataSet where = new DataSet();
            where.put("username", "yuraTest");

            DataSet output = new DataSet();
            output.put("password", "changePass");
            databaseManager.update("errorTableName", where, output);
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: relation \"errortablename\" does not exist\n" +
                "  Position: 8");
    }

    @Test
    public void isConnectionTest() {
        assertEquals(true, databaseManager.isConnected());
    }


    @Test
    public void getTableColumnsNamesTest() {
        String[] result = databaseManager.getTableColumnsNames("users");
        assertEquals("[user_id, username, password]", Arrays.toString(result));
    }

    @Test
    public void getTableColumnsNamesWithErrorTest() {
        String[] result = databaseManager.getTableColumnsNames("errorTableName");
        assertEquals("[]", Arrays.toString(result));
    }

    @Test
    public void deleteDataTest() throws RequestErrorException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "testName");
        input.put("user_id", "10");
        input.put("password", "testPass");

        databaseManager.insertData("users", input);

        DataSet[] users = databaseManager.findData("users");
        DataSet user = users[0];

        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[10, testName, testPass]", Arrays.toString(user.getValues()));

        DataSet del = new DataSet();
        del.put("username", "testName");

        databaseManager.deleteRecords("users", del);

        DataSet[] results = databaseManager.findData("users");


        assertEquals(0, results.length);
    }

    @Test
    public void deleteDataWithError() {
        String error = "";
        try {
            DataSet del = new DataSet();
            del.put("username", "testName");

            databaseManager.deleteRecords("errorTableName", del);
        } catch (Exception e) {
            error += e.getMessage();
        }

        assertEquals(error, "ERROR: relation \"errortablename\" does not exist\n" +
                "  Position: 13");
    }
}
