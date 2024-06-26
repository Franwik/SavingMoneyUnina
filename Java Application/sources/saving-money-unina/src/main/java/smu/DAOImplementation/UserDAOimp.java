package smu.DAOImplementation;

import java.sql.*;
import java.time.*;

import smu.DAO.UserDAO;
import smu.DTO.User;
import smu.Database;

public class UserDAOimp implements UserDAO {

    @Override
    public User getByEmail(String email) throws SQLException {
        
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

            user = new User(name, surname, CF, dateOfBirth, Aemail, username, password, address);
        }

        rs.close();

        ps.close();

        

        return user;
    }

    @Override
    public User getByCF(String cf) throws SQLException {
        
        Connection con = Database.getConnection();
        User user = null;

        String sql = "SELECT * FROM smu.user WHERE cf = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, cf);
        
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

            user = new User(name, surname, CF, dateOfBirth, Aemail, username, password, address);
        }

        rs.close();

        ps.close();

        

        return user;
    }

    @Override
    public int insert(User user) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO smu.user (email, username, password, address, name, surname, cf, dateofbirth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, user.getEmail());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getAddress());
        ps.setString(5, user.getName());
        ps.setString(6, user.getSurname());
        ps.setString(7, user.getCF());
        ps.setDate(8, java.sql.Date.valueOf(user.getDateOfBirth()));

        int result = ps.executeUpdate();

        ps.close();

        

        return result;
    }

    @Override
    public User checkLogin(String email, String password) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        String sql = "SELECT * FROM smu.user WHERE email = ? AND password = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);
        
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            user = new User(rs.getString("name"), rs.getString("surname"), rs.getString("cf"), rs.getDate("dateofbirth").toLocalDate(), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("address"));
        }

        rs.close();

        ps.close();

        

        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "UPDATE smu.user SET username = ?, password = ?, address = ?, name = ?, surname = ?, cf = ?, dateofbirth = ? WHERE email = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getAddress());
        ps.setString(4, user.getName());
        ps.setString(5, user.getSurname());
        ps.setString(6, user.getCF());
        ps.setDate(7, java.sql.Date.valueOf(user.getDateOfBirth()));
        ps.setString(8, user.getEmail());

        int result = ps.executeUpdate();

        ps.close();

        

        return result;
    }

    @Override
    public int delete(String email) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM smu.user WHERE email = ? CASCADE";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);

        int result = ps.executeUpdate();

        ps.close();

        

        return result;
    }

    
}
