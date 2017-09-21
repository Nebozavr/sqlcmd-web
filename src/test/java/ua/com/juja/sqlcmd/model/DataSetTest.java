package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataSetTest {
    private DataSet input;

    @Before
    public void start(){
        input = new DataSet();
        input.put("username", "yura22");
        input.put("password", "passTest");
        input.put("user_id", "10");

    }

    @Test
    public void printTest(){
        String result = input.toString();

        assertEquals("DataSet{\n" +
                "names: [username, password, user_id]\n" +
                "values: [yura22, passTest, 10]\n" +
                "}", result);
    }
}