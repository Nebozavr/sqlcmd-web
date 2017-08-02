package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListTablesTest {

    @Before
    public void setup() {
        try {
            DataBaseManager.connectionToDataBase("sqlcmd", "yura", "yura1990");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query() {
        ListTables.query();
        //assertEquals();
    }
}
