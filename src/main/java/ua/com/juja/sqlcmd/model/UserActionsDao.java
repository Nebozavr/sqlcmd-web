package ua.com.juja.sqlcmd.model;

import java.util.List;

public interface UserActionsDao {

    void log(String userName, String dbName, String actions);

    List<UserAction> getActionsForUser(String userName);
}
