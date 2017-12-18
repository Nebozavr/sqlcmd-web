package ua.com.juja.sqlcmd.service;

import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PgSQLDatabaseManager;

@Component
public class DataBaseManagerFactory {

    public DatabaseManager createManager(){
        return new PgSQLDatabaseManager();
    }
}
