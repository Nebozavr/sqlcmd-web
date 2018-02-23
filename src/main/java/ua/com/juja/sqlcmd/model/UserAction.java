package ua.com.juja.sqlcmd.model;

public class UserAction {

    public UserAction() {
    }

    public UserAction(String userName, String dbName, String actions) {
        this.userName = userName;
        this.dbName = dbName;
        this.actions = actions;
    }

    private String userName;
    private String dbName;
    private String actions;
    private int id;

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

}
