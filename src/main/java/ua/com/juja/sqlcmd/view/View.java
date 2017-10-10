package ua.com.juja.sqlcmd.view;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

public interface View {

    void write(String message);

    void writeTable(String tableName, DatabaseManager manager) throws PgSQLDatabaseManagerException;

    void writeError(Exception e);

    String read();

}
