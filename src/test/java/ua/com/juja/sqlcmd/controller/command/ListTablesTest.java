package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListTablesTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ListTables(view, databaseManager);
    }

    @Test
    public void testListTablesCanProcess() {
        assertTrue(command.canProcess("list"));
    }

    @Test
    public void testWrongListTablesCanProcess() {
        assertFalse(command.canProcess("lost"));
    }

    @Test
    public void testListTablesProcess() throws PgSQLDatabaseManagerException, WrongNumberParametersException {
        Set<String> tableNames = new HashSet<>(Arrays.asList(new  String[]{"users","invoices"}));
        when(databaseManager.listTables()).thenReturn(tableNames);
        command.process("list");
        verify(view).write(tableNames.toString());
    }

    @Test
    public void testListTablesSQLError() throws PgSQLDatabaseManagerException, WrongNumberParametersException {
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("error");
        when(databaseManager.listTables()).thenThrow(error);
        command.process("list");
        verify(databaseManager).listTables();
        verify(view).writeError(error);
    }

    @Test
    public void testListTablesProcessEmpty() throws PgSQLDatabaseManagerException, WrongNumberParametersException {
        Set<String> tableNames = new HashSet<>();
        when(databaseManager.listTables()).thenReturn(tableNames);
        command.process("list");
        verify(view).write("[]");
    }
}
