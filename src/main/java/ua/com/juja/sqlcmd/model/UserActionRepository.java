package ua.com.juja.sqlcmd.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.com.juja.sqlcmd.model.entity.UserAction;

import java.util.List;

public interface UserActionRepository extends CrudRepository<UserAction, Integer>{

    @Query(value = "SELECT ua FROM UserAction ua WHERE ua.databaseConnection.userName = :userName")
    List<UserAction> findAllByUserName(@Param("userName") String userName);
}
