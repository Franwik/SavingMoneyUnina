package smu.DAO;

import smu.DTO.User;
import java.sql.SQLException;

public interface UserDAO {

    User get(String email) throws SQLException;

    public int insert(User user) throws SQLException;

    User checkLogin(String email, String password) throws SQLException;

}
