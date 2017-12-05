package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PgSQLDatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> menuList() {
        return Arrays.asList("list", "disconnect", "help");
    }

    @Override
    public List<String> commandList() {
        return Arrays.asList("clear", "drop", "insert", "update");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException {
        DatabaseManager manager = new PgSQLDatabaseManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public DatabaseManager disconnect(DatabaseManager manager) throws PgSQLDatabaseManagerException {
        manager.disconnect();
        return null;
    }

    @Override
    public Set<String> listTables(DatabaseManager manager) throws PgSQLDatabaseManagerException {
       return manager.listTables();
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        List<List<String>> result = new LinkedList<>();

        List<String> tableColumnsNames = new ArrayList<>(manager.getTableColumnsNames(tableName));
        List<DataSet> tableData = manager.findData(tableName);

        result.add(tableColumnsNames);


        for (int i = 0; i < tableData.size(); i++) {
            List<String> values = (List<String>) (Object)tableData.get(i).getValues();
            result.add(values);
        }

        return result;
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        manager.clearTable(tableName);
    }

    @Override
    public void drop(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        manager.dropTable(tableName);
    }
}
