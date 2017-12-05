package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.List;
import java.util.Set;

public interface Service {

    List<String> menuList();

    List<String> commandList();

    DatabaseManager connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException;

    DatabaseManager disconnect(DatabaseManager manager) throws PgSQLDatabaseManagerException;

    Set<String> listTables(DatabaseManager db_manager) throws PgSQLDatabaseManagerException;

    List<List<String>> find(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException;

    void clear(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException;

    void drop(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException;
}
