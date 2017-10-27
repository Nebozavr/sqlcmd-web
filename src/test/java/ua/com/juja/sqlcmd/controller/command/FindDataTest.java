package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FindDataTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new FindData(view, databaseManager);
    }

    @Test
    public void testFindDataCanProcess() {
        assertTrue(command.canProcess("find|users"));
    }

    @Test
    public void testFindDataCanProcessError() {
        assertFalse(command.canProcess("search|users"));
    }

    @Test
    public void testFindDataProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        command.process("find|" + tableName);
        verify(view).writeTable(tableName, databaseManager);
    }

    @Test
    public void testFindDataWrongTable() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "Error";
        when(databaseManager.hasTable(tableName)).thenReturn(false);
        command.process("find|" + tableName);
        verify(view).write(String.format("Table %s doesn't exists!", tableName));
    }

    @Test
    public void testFindDataSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(view).writeTable(tableName, databaseManager);
        command.process("find|" + tableName);
        verify(view).writeError(error);
    }

    @Test
    public void testFindDataProcessWithWrongParameters() {
        try {
            command.process("find");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like find|tableName, but you enter: find", e.getMessage());
        }
    }
}
