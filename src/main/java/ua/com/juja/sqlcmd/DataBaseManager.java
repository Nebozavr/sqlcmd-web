package ua.com.juja.sqlcmd;

public interface DataBaseManager {
    void connect(String database, String user, String password);

    void clearTable(String tableName);

    void createTable(String tableName, String... columns);

    void deleteRecords(String tableName, String column, String value);

    void dropTable(String tableName);

    DataSet[] findData(String tableName);

    void insertData(String tableName, DataSet input);

    String[] listTables();

    void update(String tableName, DataSet dataWhere, DataSet dataSet);
}
