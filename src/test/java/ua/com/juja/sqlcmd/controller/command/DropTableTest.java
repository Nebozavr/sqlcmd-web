package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropTableTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropTable(view, databaseManager);
    }

    @Test
    public void testDropTableCanProcess() {
        assertTrue(command.canProcess("drop|users"));
    }

    @Test
    public void testDropTableCanProcessError() {
        assertFalse(command.canProcess("deltable|users"));
    }

    @Test
    public void testDropTableProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        command.process("drop|" + tableName);
        verify(databaseManager).dropTable(tableName);
        verify(view).write(String.format("Table %s was delete", tableName));
    }

    @Test
    public void testDropTableIsExistsTableName() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(false);
        command.process("drop|" + tableName);
        verify(view).write(String.format("Table %s doesn't exists!", tableName));

    }

    @Test
    public void testDropTableSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("Error");
        doThrow(error).when(databaseManager).dropTable(tableName);
        command.process("drop|" + tableName);
        verify(databaseManager).dropTable(tableName);
        verify(view).writeError(error);
    }

    @Test
    public void testDropTableProcessWithWrongParameters() {
        try {
            command.process("drop|users|invoices");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like " +
                    "drop|tableName, but you enter: drop|users|invoices", e.getMessage());
        }
    }
}
