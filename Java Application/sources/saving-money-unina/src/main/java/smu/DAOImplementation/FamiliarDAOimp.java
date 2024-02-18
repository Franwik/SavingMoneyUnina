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
    public int save(Familiar t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(Familiar t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public int update(Familiar t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(Familiar t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Familiar> getAll() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
