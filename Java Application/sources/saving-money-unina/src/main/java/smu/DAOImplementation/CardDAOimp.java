package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.Database;
import smu.DAO.CardDAO;
import smu.DTO.Card;

public class CardDAOimp implements CardDAO{

    @Override
    public List<Card> getByEmail(String email) throws SQLException {
        Connection con = Database.getConnection();
        List<Card> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.card WHERE owneremail = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Card card = new Card(rs.getString("iban"),rs.getString("cvv") , rs.getDate("expiredata").toLocalDate(), rs.getString("cardtype"), rs.getInt("ba_number"), rs.getString("ownercf"), rs.getString("owneremail"));
            result.add(card);
        }

        return result;
    }

    @Override
    public List<Card> getByCF(String CF) throws SQLException {
        Connection con = Database.getConnection();
        List<Card> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.card WHERE ownercf = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, CF);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Card card = new Card(rs.getString("iban"),rs.getString("cvv") , rs.getDate("expiredata").toLocalDate(), rs.getString("cardtype"), rs.getInt("ba_number"), rs.getString("ownercf"), rs.getString("owneremail"));
            result.add(card);
        }

        return result;
    }



}
