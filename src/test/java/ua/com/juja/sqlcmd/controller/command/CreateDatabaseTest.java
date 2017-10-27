package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateDatabaseTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateDatabase(view, databaseManager);
    }

    @Test
    public void testCreateDatabaseCanProcess() {
        assertTrue(command.canProcess("createDB|test"));
    }

    @Test
    public void testCreateDatabaseCanProcessError() {
        assertFalse(command.canProcess("addDB"));
    }

    @Test
    public void testCreateDatabaseProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String dbName = "test";
        command.process("createDB|" + dbName);
        verify(databaseManager).createDataBase(dbName);
        verify(view).write(String.format("Database %s was created", dbName));
    }

    @Test
    public void testCreateDatabaseSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String dbName = "test";
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        doThrow(error).when(databaseManager).createDataBase(dbName);
        command.process("createDB|" + dbName);
        verify(databaseManager).createDataBase(dbName);
        verify(view).writeError(error);
    }

    @Test
    public void testCreateDatabaseProcessWithWrongParameters() {
        try {
            command.process("createDB|test|postgres");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like " +
                    "createDB|databaseName, but you enter: createDB|test|postgres", e.getMessage());
        }
    }
}
