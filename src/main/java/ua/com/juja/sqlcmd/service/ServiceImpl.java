package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.UserAction;
import ua.com.juja.sqlcmd.model.UserActionsDao;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.*;

@Component
public abstract class ServiceImpl implements Service {

    @Autowired
    private UserActionsDao userActionsDao;

    public abstract DatabaseManager getDatabaseManager();

    @Override
    public List<String> menuList() {
        return Arrays.asList("list", "disconnect", "actions");
    }

    @Override
    public List<String> commandList() {
        return Arrays.asList("clear", "drop", "insert", "update");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) throws PgSQLDatabaseManagerException {
        DatabaseManager manager = getDatabaseManager();
        manager.connect(databaseName, userName, password);
        userActionsDao.log(userName, databaseName, "CONNECT");
        return manager;
    }

    @Override
    public DatabaseManager disconnect(DatabaseManager manager) throws PgSQLDatabaseManagerException {
        manager.disconnect();
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "DISCONNECT");
        return null;
    }

    @Override
    public Set<String> listTables(DatabaseManager manager) throws PgSQLDatabaseManagerException {
       userActionsDao.log(manager.getUserName(), manager.getDatabase(), "LIST TABLES");
       return manager.listTables();
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        List<List<String>> result = new LinkedList<>();

        List<String> tableColumnsNames = new ArrayList<String>(manager.getTableColumnsNames(tableName)) {
        };
        List<DataSet> tableData = manager.findData(tableName);

        result.add(tableColumnsNames);


        for (DataSet aTableData : tableData) {
            LinkedList<String> values = (LinkedList<String>) (Object) aTableData.getValues();
            result.add(values);
        }
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "FIND DATA FROM " + tableName);
        return result;
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "CLEAR TABLE " + tableName);
        manager.clearTable(tableName);
    }

    @Override
    public void drop(DatabaseManager manager, String tableName) throws PgSQLDatabaseManagerException {
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "DROP TABLE " + tableName);
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
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "INSERT DATA TO TABLE " + tableName);
    }

    @Override
    public void delete(DatabaseManager manager, String tableName, String columnName, String value) throws PgSQLDatabaseManagerException {
        DataSet dataSet = new DataSet();

        dataSet.put(columnName, value);

        manager.deleteRecords(tableName, dataSet);
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "DELETE DATA FROM TABLE " + tableName);
    }

    @Override
    public void update(DatabaseManager manager, String tableName, List<String> result) throws PgSQLDatabaseManagerException {
        DataSet dataWhere = new DataSet();
        DataSet dataSet = new DataSet();

        dataWhere.put(result.get(0), result.get(1));
        dataSet.put(result.get(2), result.get(3));

        manager.update(tableName, dataWhere, dataSet);
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "UPDATE DATA FROM TABLE " + tableName);
    }

    @Override
    public void createTable(DatabaseManager manager, String tableName, List<String> columns) throws PgSQLDatabaseManagerException {
        StringBuilder sql = new StringBuilder();

        for (String column : columns) {
            sql.append(column + ",");
        }

        sql.delete(sql.length() - 1, sql.length());

        String res = sql.toString();

        manager.createTable(tableName, res);
        userActionsDao.log(manager.getUserName(), manager.getDatabase(), "CREATE TABLE " + tableName);
    }


    @Override
    public List<UserAction> getActionsForUser(String userName){
        return userActionsDao.getActionsForUser(userName);
    }
}
