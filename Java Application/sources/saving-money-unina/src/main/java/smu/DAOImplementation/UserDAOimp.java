package smu.DAOImplementation;

import java.sql.*;
import java.util.List;
import java.time.*;

import smu.DAO.UserDAO;
import smu.DTO.User;
import smu.Database;

public class UserDAOimp implements UserDAO {

    @Override
    public User get(String email) throws SQLException {
        
        Connection con = Database.getConnection();
        User user = null;

        String sql = "SELECT * FROM smu.user WHERE email = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            String Aemail = rs.getString("email");
            String username = rs.getString("username");;
            String password = rs.getString("password");
            String address = rs.getString("address");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String CF = rs.getString("cf");
            LocalDate dateOfBirth = rs.getDate("dateofbirth").toLocalDate();

            user = new User(Aemail, username, password, address, name, surname, CF, dateOfBirth);
        }

        return user;
    }

    @Override
    public List<User> getAll() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public int save(User user) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(User user) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public int update(User user) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean checkLogin(String email, String password) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "SELECT * FROM smu.user WHERE email = ? AND password = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);
        
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return true;
        }

        return false;
    }

}
