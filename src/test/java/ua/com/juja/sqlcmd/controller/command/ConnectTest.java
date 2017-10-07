package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(view, databaseManager);
    }

    @Test
    public void testConnectCanProcess() {
        assertTrue(command.canProcess("connect|"));
    }

    @Test
    public void testWrongConnectCanProcess() {
        assertFalse(command.canProcess("connek"));
    }

    @Test
    public void testConnectProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String database = "sqlcmd";
        String userName = "postgres";
        String password = "postgres";
        String connectCommand = ("connect|" + database + "|" + userName + "|" + password);
        when(databaseManager.isConnected()).thenReturn(false);
        command.process(connectCommand);
        verify(databaseManager).connect(database, userName, password);
        verify(view).write("Connection was successful!");
    }

    @Test
    public void testConnectProcessAfterConnect() throws WrongNumberParametersException {
        String database = "sqlcmd";
        String userName = "postgres";
        String password = "postgres";
        String connectCommand = ("connect|" + database + "|" + userName + "|" + password);
        when(databaseManager.isConnected()).thenReturn(true);
        command.process(connectCommand);
        verify(view).write("You are already was connect to DB.");
    }

    @Test
    public void testConnectWithError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String database = "sqlcmd";
        String userName = "postgres";
        String password = "postgres";
        String connectCommand = ("connect|" + database + "|" + userName + "|" + password);
        when(databaseManager.isConnected()).thenReturn(false);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("Connection Error");
        doThrow(error).when(databaseManager).connect(database, userName, password);
        command.process(connectCommand);
        verify(databaseManager).connect(database, userName, password);
        verify(view).writeError(error);
    }

    @Test
    public void testConnectProcessWithWrongParameters() {
        try {
            command.process("connect|sqlcmd");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("The entered number of parameters is not correct. " +
                    "Must be 4 param, but you enter: 2", e.getMessage());
        }
    }
}
