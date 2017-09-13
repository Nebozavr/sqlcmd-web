package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.exceptions.BadConnectionException;
import ua.com.juja.sqlcmd.model.exceptions.NoDriverException;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;

public interface DatabaseManager {

    void connect(String database, String userName, String password) throws NoDriverException, BadConnectionException;

    String[] listTables() throws RequestErrorException;

    void clearTable(String tableName) throws RequestErrorException;

    void dropTable(String tableName) throws RequestErrorException;

    void createTable(String tableName, String... columns) throws RequestErrorException;

    DataSet[] findData(String tableName) throws RequestErrorException;

    void insertData(String tableName, DataSet input) throws RequestErrorException;

    void update(String tableName, DataSet dataWhere, DataSet dataSet) throws RequestErrorException;

    void deleteRecords(String tableName, DataSet input) throws RequestErrorException;

    String[] getTableColumnsNames(String tableName) throws RequestErrorException;

    boolean isConnected();
}
