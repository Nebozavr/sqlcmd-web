package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateTableTest {
    private View view;
    private DatabaseManager databaseManager;
    private Command command;

    @Before
    public void init() {
        databaseManager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateTable(view, databaseManager);
    }

    @Test
    public void testCreateTablesCanProcess() {
        assertTrue(command.canProcess("create|users|id int"));
    }

    @Test
    public void testCreateTablesCanProcessError() {
        assertFalse(command.canProcess("addTAble"));
    }

    @Test
    public void testCreateTablesProcess() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(false);
        command.process("create|" + tableName + "|id int");
        verify(databaseManager).createTable(tableName,"id int");
        verify(view).write(String.format("Table %s was created!", tableName));
    }

    @Test
    public void testCreateTableIsExistsTableName() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(true);
        command.process("create|" + tableName + "|id int");
        verify(view).write(String.format("Table wih name %s is already exists!", tableName));

    }

    @Test
    public void testCreateTablesSQLError() throws WrongNumberParametersException, PgSQLDatabaseManagerException {
        String tableName = "users";
        when(databaseManager.hasTable(tableName)).thenReturn(false);
        PgSQLDatabaseManagerException error = new PgSQLDatabaseManagerException("Error");
        doThrow(error).when(databaseManager).createTable(tableName,"id int");
        command.process("create|" + tableName + "|id int");
        verify(databaseManager).createTable(tableName,"id int");
        verify(view).writeError(error);
    }

    @Test
    public void testCreateTableProcessWithWrongParameters() {
        try {
            command.process("create|users");
            fail();
        } catch (WrongNumberParametersException e) {
            assertEquals("Error entering command, must be like " +
                    "create|tableName|column1Name fieldType,...,columnNName fieldType, but you enter: create|users", e.getMessage());
        }
    }
}
