package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteRowsTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DeleteRows(view, databaseManager);
    }

    @Test
    public void testDeleteRowsCanProcess() {
        assertTrue(command.canProcess("delete|users|id|1"));
    }

    @Test
    public void testDeleteRowsCanProcessError() {
        assertFalse(command.canProcess("del|users"));
    }

    @Test
    public void testDeleteRowsProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        DataSet dataSet = new DataSet();
        dataSet.put(columnName, value);
        String deleteCommand = ("delete|" + tableName + "|" + columnName + "|" + value);
        command.process(deleteCommand);
        verify(databaseManager).deleteRecords(eq(tableName) ,any(DataSet.class));
        verify(view).write(String.format("The data was delete from table: %s", tableName));
    }


    @Test
    public void testDeleteRowsWithError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        String columnName = "id";
        String value = "1";
        String deleteCommand = ("delete|" + tableName + "|" + columnName + "|" + value);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(databaseManager).deleteRecords(eq(tableName), any(DataSet.class));
        command.process(deleteCommand);
        verify(databaseManager, times(1)).deleteRecords(eq(tableName), any(DataSet.class));
        verify(view).writeError(error);

    }

    @Test
    public void testDeleteRowsProcessWithWrongParameters() {
        try {
            command.process("delete|users");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like delete|tableName|columnName|value, but you enter:delete|users", e.getMessage());
        }
    }
}
