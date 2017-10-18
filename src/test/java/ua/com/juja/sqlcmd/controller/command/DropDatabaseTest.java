package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropDatabaseTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropDatabase(view, databaseManager);
    }

    @Test
    public void testDropDatabaseCanProcess() {
        assertTrue(command.canProcess("dropDB|test"));
    }

    @Test
    public void testDropDatabaseCanProcessError() {
        assertFalse(command.canProcess("delDB"));
    }

    @Test
    public void testDropDatabaseProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String dbName = "test";
        command.process("dropDB|" + dbName);
        verify(databaseManager).dropDatabase(dbName);
        verify(view).write(String.format("Database %s was delete", dbName));
    }

    @Test
    public void testDropDatabaseSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String dbName = "test";
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(databaseManager).dropDatabase(dbName);
        command.process("dropDB|" + dbName);
        verify(databaseManager).dropDatabase(dbName);
        verify(view).writeError(error);
    }

    @Test
    public void testDropDatabaseProcessWithWrongParameters() {
        try {
            command.process("dropDB|test|postgres");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like " +
                    "dropDB|databaseName, but you enter:dropDB|test|postgres", e.getMessage());
        }
    }
}
