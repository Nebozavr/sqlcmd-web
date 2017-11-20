package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.List;

public interface Service {

    List<String> commandsList();

    void connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException;
}
