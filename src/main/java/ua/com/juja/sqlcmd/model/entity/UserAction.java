package ua.com.juja.sqlcmd.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_actions")
public class UserAction {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;



    @JoinColumn(name = "db_connection_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DatabaseConnection databaseConnection;

    private String actions;

    public UserAction() {
    }

    public UserAction(String actions, DatabaseConnection databaseConnection) {
        this.actions = actions;
        this.databaseConnection = databaseConnection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

}
