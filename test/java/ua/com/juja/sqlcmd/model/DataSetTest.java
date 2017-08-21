package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataSetTest {

    DataSet input;
    @Before
    public void start(){

        input = new DataSet();
        input.put("username", "yura22");

    }

    @Test
    public void getTest(){
        Object test = input.get("username");
        assertEquals(test, "yura22");
    }

    @Test
    public void getWithErrorTest(){
        Object test = input.get("errorName");
        assertEquals(test, null);
    }

    @Test
    public void printTest(){
        String result = input.toString();

        assertEquals(result, "DataSet{\n" +
                "names: [username]\n" +
                "values: [yura22]\n" +
                "}");
    }
}