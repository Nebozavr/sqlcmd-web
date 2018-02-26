package ua.com.juja.sqlcmd.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserActionsDaoImpl implements UserActionsDao {

    private JdbcTemplate template;

    @Autowired
    @Qualifier("logDataSource")
    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void log(String userName, String dbName, String actions) {
        template.update("INSERT INTO user_actions (user_name, db_name, actions) " +
                        "VALUES (?, ?, ?)",
                userName, dbName, actions);
    }

    @Override
    public List<UserAction> getActionsForUser(String userName) {
        return template.query("SELECT * FROM user_actions WHERE user_name = ?", new Object[]{userName},
                new RowMapper<UserAction>() {
                    @Override
                    public UserAction mapRow(ResultSet resultSet, int i) throws SQLException {
                        UserAction result = new UserAction();
                        result.setId(resultSet.getInt("id"));
                        result.setUserName(resultSet.getString("user_name"));
                        result.setDbName(resultSet.getString("db_name"));
                        result.setActions(resultSet.getString("actions"));
                        return result;
                    }
                });
    }
}
