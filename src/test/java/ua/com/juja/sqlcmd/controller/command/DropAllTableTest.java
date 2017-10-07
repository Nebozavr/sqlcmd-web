package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropAllTableTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropAllTable(view, databaseManager);
    }

    @Test
    public void testDropAllTableCanProcess() {
        assertTrue(command.canProcess("dropAll"));
    }

    @Test
    public void testDropAllTableCanProcessError() {
        assertFalse(command.canProcess("deltableAll"));
    }

    @Test
    public void testDropAllTableProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        command.process("dropAll");
        verify(databaseManager).dropAllTable();
        verify(view).write("All tables have been deleted");
    }

    @Test
    public void testDropAllTableSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("Error");
        doThrow(error).when(databaseManager).dropAllTable();
        command.process("dropAll");
        verify(databaseManager).dropAllTable();
        verify(view).writeError(error);
    }
}
