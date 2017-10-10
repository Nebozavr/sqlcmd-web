package ua.com.juja.sqlcmd.view;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.utils.PrintTable;

import java.util.Scanner;

public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void writeTable(String tableName, DatabaseManager manager) throws PgSQLDatabaseManagerException {
        PrintTable.printTable(tableName, manager);
    }

    @Override
    public void writeError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null && e.getCause().equals(e.getMessage())) {
            message += " " + e.getCause().getMessage();
        }
        System.out.println(message);
        System.out.println("Please try again");
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return  scanner.nextLine();
    }
}
