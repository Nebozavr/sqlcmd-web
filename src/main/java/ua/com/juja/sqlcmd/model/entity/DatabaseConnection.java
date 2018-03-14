package ua.com.juja.sqlcmd.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "db_connection")
public class DatabaseConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "db_name")
    private String dbName;


    public DatabaseConnection() {
    }

    public DatabaseConnection(String userName, String dbName) {
        this.userName = userName;
        this.dbName = dbName;
    }

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

}
