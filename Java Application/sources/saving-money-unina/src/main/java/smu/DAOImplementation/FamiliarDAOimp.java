package smu.DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import smu.Database;
import smu.DAO.FamiliarDAO;
import smu.DTO.Familiar;

public class FamiliarDAOimp implements FamiliarDAO{
    
    @Override
    public Familiar getByCF(String CF) throws SQLException {
        Connection con = Database.getConnection();
        Familiar result = null;

        String sql = "SELECT * FROM smu.familiar WHERE cf = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, CF);
        
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            result = new Familiar(rs.getString("name"), rs.getString("surname"), rs.getString("cf"), rs.getDate("dateofbirth").toLocalDate(), rs.getString("familiaremail"));
        }

        return result;
    }
    
    @Override
    public List<Familiar> getByEmail(String email) throws SQLException {
        Connection con = Database.getConnection();
        List<Familiar> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.familiar WHERE familiaremail = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Familiar familiar = new Familiar(rs.getString("name"), rs.getString("surname"), rs.getString("cf"), rs.getDate("dateofbirth").toLocalDate(), rs.getString("familiaremail"));
            result.add(familiar);
        }

        return result;
    }

    @Override
    public int insert(Familiar familiar) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO smu.familiar (name, surname, cf, dateofbirth, familiaremail) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, familiar.getName());
        ps.setString(2, familiar.getSurname());
        ps.setString(3, familiar.getCF());
        ps.setDate(4, java.sql.Date.valueOf(familiar.getDateOfBirth()));
        ps.setString(5, familiar.getFamiliarEmail());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int delete(String CF) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM smu.familiar WHERE cf = ? CASCADE";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, CF);

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int update(Familiar familiar) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "UPDATE smu.familiar SET name = ?, surname = ?, dateofbirth = ?, familiaremail = ? WHERE cf = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, familiar.getName());
        ps.setString(2, familiar.getSurname());
        ps.setDate(3, java.sql.Date.valueOf(familiar.getDateOfBirth()));
        ps.setString(4, familiar.getFamiliarEmail());
        ps.setString(5, familiar.getCF());

        int result = ps.executeUpdate();

        return result;
    }
}
