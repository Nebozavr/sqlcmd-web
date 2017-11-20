package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PgSQLDatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {
    private DatabaseManager manager;

    public ServiceImpl() {
        manager = new PgSQLDatabaseManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException {
        manager.connect(databaseName, userName, password);
    }
}
