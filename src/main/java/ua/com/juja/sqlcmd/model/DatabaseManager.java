package ua.com.juja.sqlcmd.model;

public interface DatabaseManager {

    void connect(String database, String userName, String password);

    String[] listTables();

    void clearTable(String tableName);

    void dropTable(String tableName);

    void createTable(String tableName, String... columns);

    DataSet[] findData(String tableName);

    void insertData(String tableName, DataSet input);

    void update(String tableName, DataSet dataWhere, DataSet dataSet);

    void deleteRecords(String tableName, DataSet input);

    void exit();

    String[] getTableColumnsNames(String tableName);

    boolean isConnected();
}
