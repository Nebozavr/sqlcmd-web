package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClearTableTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ClearTable(view, databaseManager);
    }

    @Test
    public void testClearTablesCanProcess() {
        assertTrue(command.canProcess("clear|users"));
    }

    @Test
    public void testClearTablesCanProcessError() {
        assertFalse(command.canProcess("clear"));
    }

    @Test
    public void testClearTablesProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        command.process("clear|" + tableName);
        verify(databaseManager).clearTable(tableName);
        verify(view).write("Table users was cleared");
    }

    @Test
    public void testClearWrongTable() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "wrongName";
        when(databaseManager.hasTable(tableName)).thenReturn(false);
        command.process("clear|" + tableName);
        verify(view).write(String.format("Table %s doesn't exists!", tableName));

    }

    @Test
    public void testClearTableProcessWithWrongParameters() {
        try {
            command.process("clear");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like clear|tableName, but you enter:clear", e.getMessage());
        }
    }
}
