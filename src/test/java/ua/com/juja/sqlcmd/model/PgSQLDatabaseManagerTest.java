package ua.com.juja.sqlcmd.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PgSQLDatabaseManagerTest {
    private static final String DATABASE = "sqlcmd";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private DatabaseManager databaseManager;

    @Before
    public void setup() {
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

    @Test(expected = BadConnectionException.class)
    public void testConnectionWithError() throws NoDriverException, BadConnectionException {
        DatabaseManager testManager = new PgSQLDatabaseManager();
        testManager.connect(DATABASE, USER, "errorPass");
    }

    @Test
    public void testListTable() throws RequestErrorException {
        String result = "[roles, users]";

        assertEquals(result, databaseManager.listTables().toString());
    }

    @Test
    public void testDropTable() throws RequestErrorException {
        databaseManager.dropTable("roles");

        String result = "[users]";
        assertEquals(result, databaseManager.listTables().toString());

    }

    @Test(expected = RequestErrorException.class)
    public void testDropTableWithError() throws RequestErrorException {
        databaseManager.dropTable("errorTableName");
    }

    @Test(expected = RequestErrorException.class)
    public void testCreateTableWithError() throws RequestErrorException {
        databaseManager.createTable("test", " integer", "name text");
    }

    @Test
    public void testFindData() throws RequestErrorException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yura22");
        input.put("id", "1");
        input.put("password", "qwerty");

        databaseManager.insertData("users", input);

        DataSet[] users = databaseManager.findData("users");

        DataSet user = users[0];
        assertEquals("[id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[1, yura22, qwerty]", Arrays.toString(user.getValues()));

        databaseManager.clearTable("users");
    }

    @Test(expected = RequestErrorException.class)
    public void testFindDataWithError() throws RequestErrorException {
        databaseManager.findData("errorTableName");
    }

    @Test(expected = RequestErrorException.class)
    public void testInsertDataWithError() throws RequestErrorException {
        DataSet input = new DataSet();
        input.put("username", "yura22");
        input.put("user_id", "1");
        input.put("password", "qwerty");
        databaseManager.insertData("errorTableName", input);
    }

    @Test
    public void testUpdate() throws RequestErrorException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yuraTest");
        input.put("id", "2");
        input.put("password", "passwordTest");

        databaseManager.insertData("users", input);

        DataSet where = new DataSet();
        where.put("username", "yuraTest");

        DataSet output = new DataSet();
        output.put("password", "changePass");

        databaseManager.update("users", where, output);

        DataSet[] users = databaseManager.findData("users");

        DataSet user = users[0];
        assertEquals("[id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[2, yuraTest, changePass]", Arrays.toString(user.getValues()));
    }

    @Test(expected = RequestErrorException.class)
    public void testUpdateDataWithError() throws RequestErrorException {
            DataSet where = new DataSet();
            where.put("username", "yuraTest");

            DataSet output = new DataSet();
            output.put("password", "changePass");
            databaseManager.update("errorTableName", where, output);
    }

    @Test
    public void testIsConnection() {
        assertEquals(true, databaseManager.isConnected());
    }

    @Test
    public void testGetTableColumnsNames() throws RequestErrorException {
        String actual = databaseManager.getTableColumnsNames("users").toString();
        assertEquals("[id, username, password]", actual);
    }

    @Test
    public void testGetTableColumnsNamesWithError() throws RequestErrorException {
        String actual = databaseManager.getTableColumnsNames("errorTableName").toString();
        assertEquals("[]", actual);
    }

    @Test
    public void testDeleteData() throws RequestErrorException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "testName");
        input.put("id", "10");
        input.put("password", "testPass");

        databaseManager.insertData("users", input);

        DataSet[] users = databaseManager.findData("users");
        DataSet user = users[0];

        assertEquals("[id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[10, testName, testPass]", Arrays.toString(user.getValues()));

        DataSet del = new DataSet();
        del.put("username", "testName");

        databaseManager.deleteRecords("users", del);

        DataSet[] results = databaseManager.findData("users");

        assertEquals(0, results.length);
    }

    @Test(expected = RequestErrorException.class)
    public void testDeleteDataWithError() throws RequestErrorException {
            DataSet del = new DataSet();
            del.put("username", "testName");
            databaseManager.deleteRecords("errorTableName", del);
    }
}
