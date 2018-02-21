package ua.com.juja.sqlcmd.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.BadSqlGrammarException;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.utils.PropertiesLoader;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PgSQLDatabaseManagerTest {
    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String DATABASE = propertiesLoader.getDatabaseName();
    private static final String USER = propertiesLoader.getUserName();
    private static final String PASSWORD = propertiesLoader.getPassword();

    private DatabaseManager databaseManager;


    @Before
    public void setup() {
        try {
            databaseManager = new PgSQLDatabaseManager();
            databaseManager.connect(DATABASE, USER, PASSWORD);

            databaseManager.createTable("users", "id int", "userName text", "password text");
            databaseManager.createTable("roles", "roleID int", "roleName text", "description text");

        } catch (PgSQLDatabaseManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void after() {
        try {
            databaseManager.dropTable("users");
            databaseManager.dropTable("roles");
        } catch (PgSQLDatabaseManagerException e) {
            e.getMessage();
        }
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testConnectionWithError() throws PgSQLDatabaseManagerException {
        DatabaseManager testManager = new PgSQLDatabaseManager();
        testManager.connect(DATABASE, USER, "errorPass");
    }

    @Test
    public void testDisconnect() throws PgSQLDatabaseManagerException{
        DatabaseManager testManager = new PgSQLDatabaseManager();
        testManager.connect(DATABASE, USER, PASSWORD);
        testManager.disconnect();
        assertFalse(testManager.isConnected());
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testDisconnectWithError() throws PgSQLDatabaseManagerException{
        DatabaseManager testManager = new PgSQLDatabaseManager();
        testManager.disconnect();
    }

    @Test
    public void testListTable() throws PgSQLDatabaseManagerException {
        String result = "[roles, users]";

        assertEquals(result, databaseManager.listTables().toString());
    }

    @Test
    public void testDropTable() throws PgSQLDatabaseManagerException {
        databaseManager.dropTable("roles");

        String result = "[users]";
        assertEquals(result, databaseManager.listTables().toString());

    }

    @Test
    public void testDropAllTable() throws PgSQLDatabaseManagerException {
        databaseManager.dropAllTable();
        String result = "[]";
        assertEquals(result, databaseManager.listTables().toString());

    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testDropTableWithError() throws PgSQLDatabaseManagerException {
        databaseManager.dropTable("errorTableName");
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testCreateTableWithError() throws PgSQLDatabaseManagerException {
        databaseManager.createTable("test", " integer", "name text");
    }

    @Test
    public void testFindData() throws PgSQLDatabaseManagerException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yura22");
        input.put("id", "1");
        input.put("password", "qwerty");

        databaseManager.insertData("users", input);

        List<DataSet> users = databaseManager.findData("users");

        DataSet user = users.get(0);
        assertEquals("[id, username, password]", user.getNames().toString());
        assertEquals("[1, yura22, qwerty]", user.getValues().toString());

        databaseManager.clearTable("users");
    }

    @Test(expected = BadSqlGrammarException.class)
    public void testFindDataWithError() throws PgSQLDatabaseManagerException {
        databaseManager.findData("errorTableName");
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testInsertDataWithError() throws PgSQLDatabaseManagerException {
        DataSet input = new DataSet();
        input.put("username", "yura22");
        input.put("user_id", "1");
        input.put("password", "qwerty");
        databaseManager.insertData("errorTableName", input);
    }

    @Test
    public void testUpdate() throws PgSQLDatabaseManagerException {
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

        List<DataSet> users = databaseManager.findData("users");

        DataSet user = users.get(0);
        assertEquals("[id, username, password]", user.getNames().toString());
        assertEquals("[2, yuraTest, changePass]", user.getValues().toString());
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testUpdateDataWithError() throws PgSQLDatabaseManagerException {
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
    public void testGetTableColumnsNames() throws PgSQLDatabaseManagerException {
        String actual = databaseManager.getTableColumnsNames("users").toString();
        assertEquals("[id, username, password]", actual);
    }

    @Test
    public void testGetTableColumnsNamesWithError() throws PgSQLDatabaseManagerException {
        String actual = databaseManager.getTableColumnsNames("errorTableName").toString();
        assertEquals("[]", actual);
    }

    @Test
    public void testDeleteData() throws PgSQLDatabaseManagerException {
        databaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "testName");
        input.put("id", "10");
        input.put("password", "testPass");

        databaseManager.insertData("users", input);

        List<DataSet> users = databaseManager.findData("users");
        DataSet user = users.get(0);

        assertEquals("[id, username, password]", user.getNames().toString());
        assertEquals("[10, testName, testPass]", user.getValues().toString());

        DataSet del = new DataSet();
        del.put("username", "testName");

        databaseManager.deleteRecords("users", del);

        List<DataSet> results = databaseManager.findData("users");

        assertEquals(0, results.size());
    }

    @Test(expected = PgSQLDatabaseManagerException.class)
    public void testDeleteDataWithError() throws PgSQLDatabaseManagerException {
        DataSet del = new DataSet();
        del.put("username", "testName");
        databaseManager.deleteRecords("errorTableName", del);
    }
}
