package ua.com.juja.sqlcmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JDBCDataBaseManagerTest {


    String[] result = new String[]{"test", "test2", "users"};
    private DataBaseManager dataBaseManager;

    @Before
    public void setup() {
        try {
            dataBaseManager = new JDBCDataBaseManager();
            dataBaseManager.connect("sqlcmd", "yura", "yura1990");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listTableTest() {

        assertEquals(dataBaseManager.listTables(), result);
    }


    @Test
    public void findDataTest() {
        dataBaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yura2");
        input.put("user_id", "1");
        input.put("password", "qwerty");

        dataBaseManager.insertData("users", input);

        DataSet[] users = dataBaseManager.findData("users");


        DataSet user = users[0];
        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[1, yura2, qwerty]", Arrays.toString(user.getValues()));

    }

    @Test
    public void updateTest() {
        dataBaseManager.clearTable("users");

        DataSet input = new DataSet();
        input.put("username", "yuraTest");
        input.put("user_id", "2");
        input.put("password", "passwordTest");

        dataBaseManager.insertData("users", input);

        DataSet where = new DataSet();
        where.put("username", "yuraTest");

        DataSet output = new DataSet();
        output.put("password", "changePass");

        dataBaseManager.update("users", where, output);

        DataSet[] users = dataBaseManager.findData("users");


        DataSet user = users[0];
        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[2, yuraTest, changePass]", Arrays.toString(user.getValues()));


    }


}
