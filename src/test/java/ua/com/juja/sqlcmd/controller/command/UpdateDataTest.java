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

public class UpdateDataTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new UpdateData(view, databaseManager);
    }

    // "update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet";
    @Test
    public void testUpdateDataCanProcess() {
        assertTrue(command.canProcess("update|users|id|5|name|test"));
    }

    @Test
    public void testUpdateDataCanProcessError() {
        assertFalse(command.canProcess("updata|users|id|5|name|test"));
    }

    @Test
    public void testUpdateDataProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        String columnNameSet = "name";
        String valueSet = "Test";
        DataSet dataSetWhere = new DataSet();
        dataSetWhere.put(columnName, value);
        DataSet dataSetSet = new DataSet();
        dataSetSet.put(columnNameSet, valueSet);
        String updateCommand = ("insert|" + tableName + "|" + columnName + "|" + value + "|" + columnNameSet + "|" +valueSet);
        command.process(updateCommand);
        verify(databaseManager).update(eq(tableName) ,any(DataSet.class), any(DataSet.class));
        verify(view).write(String.format("Data from %s was updated", tableName));
    }


    @Test
    public void testUpdateDataSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        String columnNameSet = "name";
        String valueSet = "Test";
        DataSet dataSetWhere = new DataSet();
        dataSetWhere.put(columnName, value);
        DataSet dataSetSet = new DataSet();
        dataSetSet.put(columnNameSet, valueSet);
        String updateCommand = ("insert|" + tableName + "|" + columnName + "|" + value + "|" + columnNameSet + "|" +valueSet);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(databaseManager).update(eq(tableName) ,any(DataSet.class), any(DataSet.class));
        command.process(updateCommand);
        verify(databaseManager).update(eq(tableName) ,any(DataSet.class), any(DataSet.class));
        verify(view).writeError(error);

    }

    @Test
    public void testUpdateDataProcessWithWrongParameters() {
        try {
            command.process("update|users|id");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet, but you enter: update|users|id", e.getMessage());
        }
    }
}
