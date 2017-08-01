package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ListTablesTest {

    @Before
    public void setup(){
        try {
            ConnectionDB.connectionToDataBase("sqlcmd", "yura", "yura1990");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query(){
        ListTables.query();
        assertEquals(){
    }
}
