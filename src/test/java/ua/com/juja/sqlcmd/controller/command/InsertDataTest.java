package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InsertDataTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new InsertData(view, databaseManager);
    }

    // "insert|tableName|columnName1|value1|...|columnNameN|valueN";
    @Test
    public void testInsertDataCanProcess() {
        assertTrue(command.canProcess("insert|users|id|5"));
    }

    @Test
    public void testInsertDataCanProcessError() {
        assertFalse(command.canProcess("incept|users|id|5"));
    }

    @Test
    public void testInsertDataProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        DataSet dataSet = new DataSet();
        dataSet.put(columnName, value);
        String insertCommand = ("insert|" + tableName + "|" + columnName + "|" + value);
        command.process(insertCommand);
        verify(databaseManager).insertData(eq(tableName) ,any(DataSet.class));
        verify(view).write(String.format("New data was add to %s", tableName));
    }


    @Test
    public void testInsertDataSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        DataSet dataSet = new DataSet();
        dataSet.put(columnName, value);
        String insertCommand = ("insert|" + tableName + "|" + columnName + "|" + value);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(databaseManager).insertData(eq(tableName), any(DataSet.class));
        command.process(insertCommand);
        verify(databaseManager).insertData(eq(tableName) ,any(DataSet.class));
        verify(view).writeError(error);

    }

    @Test
    public void testInsertDataProcessWithWrongParameters() {
        try {
            command.process("insert|users|id");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like insert|tableName|columnName1|value1|...|columnNameN|valueN, but you enter: insert|users|id", e.getMessage());
        }
    }
}
