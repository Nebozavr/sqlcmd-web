package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.List;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException;

    DatabaseManager disconnect(DatabaseManager manager) throws PgSQLDatabaseManagerException;
}
