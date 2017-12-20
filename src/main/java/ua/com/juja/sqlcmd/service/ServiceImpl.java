package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.*;

@Component
public class ServiceImpl implements Service {

    @Lookup
    private DatabaseManager getDatabaseManager(){
        return null;
    }

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
        DatabaseManager manager = getDatabaseManager();
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

        List<String> tableColumnsNames = new ArrayList<String>(manager.getTableColumnsNames(tableName)) {
        };
        List<DataSet> tableData = manager.findData(tableName);

        result.add(tableColumnsNames);


        for (int i = 0; i < tableData.size(); i++) {
            LinkedList<String> values = (LinkedList<String>) (Object)tableData.get(i).getValues();
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

    @Override
    public void insert(DatabaseManager manager, String tableName, List<String> result) throws PgSQLDatabaseManagerException {
        DataSet dataSet = new DataSet();

        for (int index = 0; index < result.size() / 2; index++) {
            String columnName = result.get(index * 2);
            String value = result.get(index * 2 + 1);

            dataSet.put(columnName, value);
        }

        manager.insertData(tableName, dataSet);
    }

    @Override
    public void delete(DatabaseManager manager, String tableName, String columnName, String value) throws PgSQLDatabaseManagerException {
        DataSet dataSet = new DataSet();

        dataSet.put(columnName, value);

        manager.deleteRecords(tableName, dataSet);
    }

    @Override
    public void update(DatabaseManager manager, String tableName, List<String> result) throws PgSQLDatabaseManagerException {
        DataSet dataWhere = new DataSet();
        DataSet dataSet = new DataSet();

        dataWhere.put(result.get(0), result.get(1));
        dataSet.put(result.get(2), result.get(3));

        manager.update(tableName, dataWhere, dataSet);
    }
}
