package smu.DAO;

import smu.DTO.User;
import java.sql.SQLException;

public interface UserDAO {

    User getByEmail(String email) throws SQLException;

    User getByCF(String cf) throws SQLException;

    public int insert(User user) throws SQLException;

    public int update(User user) throws SQLException;

    public int delete(String email) throws SQLException;

    User checkLogin(String email, String password) throws SQLException;

}
