package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ExitTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Exit(view, databaseManager);
    }

    @Test
    public void testExitIsDetected() {
        assertTrue(command.canProcess("exit"));
    }

    @Test
    public void testWrongExitIsDetected() {
        assertFalse(command.canProcess("escape"));
    }

    @Test
    public void testExitExecute() throws WrongNumberParametersException {
        command.process("exit");
        verify(view).write("Bye!!!");
    }

}
