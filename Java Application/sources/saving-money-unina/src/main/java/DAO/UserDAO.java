package DAO;

import DTO.User;
import java.sql.SQLException;

public interface UserDAO extends DAO<User>{

    User get(String email) throws SQLException;

    boolean checkLogin(String email, String password) throws SQLException;

}
