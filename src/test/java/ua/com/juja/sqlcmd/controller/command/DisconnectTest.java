package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DisconnectTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Disconnect(view, databaseManager);
    }

    @Test
    public void testDisconnectCanProcess() {
        assertTrue(command.canProcess("disconnect"));
    }

    @Test
    public void testWrongDisconnectCanProcess() {
        assertFalse(command.canProcess("diskonekt"));
    }

    @Test
    public void testDisconnectProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        command.process("disconnect");
        verify(databaseManager).disconnect();
        verify(view).write("Connection was closed");
    }

    @Test
    public void testDisconnectWithError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("Connection Error");
        doThrow(error).when(databaseManager).disconnect();
        command.process("disconnect");
        verify(databaseManager).disconnect();
        verify(view).writeError(error);
    }

}
