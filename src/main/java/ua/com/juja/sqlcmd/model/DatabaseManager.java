package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void connect(String database, String userName, String password) throws PgSQLDatabaseManagerException;

    void disconnect() throws PgSQLDatabaseManagerException;

    Set<String> listTables() throws PgSQLDatabaseManagerException;

    void clearTable(String tableName) throws PgSQLDatabaseManagerException;

    void dropTable(String tableName) throws PgSQLDatabaseManagerException;

    void dropDatabase(String dbName) throws PgSQLDatabaseManagerException;

    void dropAllTable() throws PgSQLDatabaseManagerException;

    void createTable(String tableName, String... columns) throws PgSQLDatabaseManagerException;

    void createDataBase(String dbName) throws PgSQLDatabaseManagerException;

    List<DataSet> findData(String tableName) throws PgSQLDatabaseManagerException;

    void insertData(String tableName, DataSet input) throws PgSQLDatabaseManagerException;

    void update(String tableName, DataSet dataWhere, DataSet dataSet) throws PgSQLDatabaseManagerException;

    void deleteRecords(String tableName, DataSet input) throws PgSQLDatabaseManagerException;

    Set<String> getTableColumnsNames(String tableName) throws PgSQLDatabaseManagerException;

    boolean isConnected();

    boolean hasTable(String tableName) throws PgSQLDatabaseManagerException;
}
