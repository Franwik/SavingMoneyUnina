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

}
