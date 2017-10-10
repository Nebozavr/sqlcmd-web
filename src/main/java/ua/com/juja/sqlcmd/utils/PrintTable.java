package ua.com.juja.sqlcmd.utils;

import dnl.utils.text.table.TextTable;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;

import java.util.List;

public class PrintTable {

    public static void printTable(String tableName, DatabaseManager manager) throws PgSQLDatabaseManagerException {
        String[] tableColumn = manager.getTableColumnsNames(tableName).toArray(new String[0]);

        List<DataSet> result = manager.findData(tableName);

        Object[][] values = new Object[result.size()][tableColumn.length];

        for (int i = 0; i < result.size(); i++) {
            values[i] = result.get(i).getValues().toArray();
        }

        TextTable table = new TextTable(tableColumn, values);

        table.printTable();
    }
}
