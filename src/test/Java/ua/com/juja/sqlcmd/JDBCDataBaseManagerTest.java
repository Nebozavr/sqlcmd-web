package ua.com.juja.sqlcmd;

import org.junit.*;


import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JDBCDataBaseManagerTest {


    String[] result = new String[]{"test", "test2", "users"};
    private DataBaseManager DataBaseManager;

    @Before
    public void setup() {
        try {
            DataBaseManager = new JDBCDataBaseManager();
            DataBaseManager.connect("sqlcmd", "yura", "yura1990");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listTableTest() {

        assertEquals(DataBaseManager.listTables(), result);
    }

    @Test
    public void findDataTest() {
        DataBaseManager.clearTable("users");

      //  DataBaseManager.insertData("users", "user_id", "1", "username", "yura", "password", "qwerty");

        DataSet[] users = DataBaseManager.findData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[user_id, username, password]", Arrays.toString(user.getNames()));
        assertEquals("[1, yura, qwerty]", Arrays.toString(user.getValues()));

    }


}
