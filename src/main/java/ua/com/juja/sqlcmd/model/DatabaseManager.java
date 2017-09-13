package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;

public interface DatabaseManager {

    void connect(String database, String userName, String password) throws NoDriverException, BadConnectionException;

    String[] listTables() throws RequestErrorException;

    void clearTable(String tableName);

    void dropTable(String tableName);

    void createTable(String tableName, String... columns);

    DataSet[] findData(String tableName);

    void insertData(String tableName, DataSet input);

    void update(String tableName, DataSet dataWhere, DataSet dataSet);

    void deleteRecords(String tableName, DataSet input);

    String[] getTableColumnsNames(String tableName);

    boolean isConnected();
}
