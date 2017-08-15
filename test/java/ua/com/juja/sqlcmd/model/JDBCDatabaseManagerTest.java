package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class JDBCDatabaseManagerTest {



    private DatabaseManager databaseManager;

    @Before
    public void setup() {
        try {
            databaseManager = new JDBCDatabaseManager();
            databaseManager.connect("sqlcmd", "yura", "yura1990");   //Test for connection

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Test for listTables
    @Test
    public void listTableTest() {
        String[] result = new String[]{"test", "test2", "users"};

        assertArrayEquals(databaseManager.listTables(), result);
    }

    //Test for dropTable
    @Test
    public void dropTableTest() {
        databaseManager.dropTable("test");

        String[] result = new String[]{"test2", "users"};
        assertArrayEquals(databaseManager.listTables(), result);

    }

    //Test for createTable
    @Test
    public void createTableTest() {
        databaseManager.createTable("test", "id integer", "name text");


        String[] result = new String[]{"test", "test2", "users"};
        assertArrayEquals(databaseManager.listTables(), result);

    }


    //Test for findData
    @Test
    public void findDataTest() {
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

    }

    //Test for updateData
    @Test
    public void updateTest() {
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


}
