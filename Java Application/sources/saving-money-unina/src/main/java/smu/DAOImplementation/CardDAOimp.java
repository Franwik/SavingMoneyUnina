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
            Card card = new Card(rs.getString("cardnumber"), rs.getString("iban"), rs.getString("cvv"), rs.getDate("expiredata").toLocalDate(), rs.getString("cardtype"), rs.getInt("ba_number"), rs.getString("ownercf"), rs.getString("owneremail"));
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
            Card card = new Card(rs.getString("cardnumber"), rs.getString("iban"), rs.getString("cvv"), rs.getDate("expiredata").toLocalDate(), rs.getString("cardtype"), rs.getInt("ba_number"), rs.getString("ownercf"), rs.getString("owneremail"));
            result.add(card);
        }

        return result;
    }

    @Override
    public int insert(Card card) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO smu.card (cardnumber, iban, cvv, expiredata, cardtype, ba_number, ownercf, owneremail) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, card.getCardNumber());
        ps.setString(2, card.getIban());
        ps.setString(3, card.getCvv());
        ps.setDate(4, java.sql.Date.valueOf(card.getExpireDate()));
        ps.setString(5, card.getCardType());
        ps.setInt(6, card.getBa_number());
        ps.setString(7, card.getOwnerCF());
        ps.setString(8, card.getOwnerEmail());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int update(Card card) throws SQLException {

        Connection con = Database.getConnection();
        
        String sql = "UPDATE smu.card SET iban = ?, cvv = ?, expiredata = ?, cardtype = ?, ba_number = ?, ownercf = ?, owneremail = ? WHERE cardnumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, card.getIban());
        ps.setString(2, card.getCvv());
        ps.setDate(3, java.sql.Date.valueOf(card.getExpireDate()));
        ps.setString(4, card.getCardType());
        ps.setInt(5, card.getBa_number());
        ps.setString(6, card.getOwnerCF());
        ps.setString(7, card.getOwnerEmail());
        ps.setString(8, card.getCardNumber());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int delete(String cardNumber) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM smu.card WHERE cardNumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, cardNumber);

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public Card getByNumber(String cardNumber) throws SQLException {
        Connection con = Database.getConnection();
        Card card = null;

        String sql = "SELECT * FROM smu.card WHERE cardnumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, cardNumber);
        
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            card = new Card(rs.getString("cardnumber"), rs.getString("iban"), rs.getString("cvv"), rs.getDate("expiredata").toLocalDate(), rs.getString("cardtype"), rs.getInt("ba_number"), rs.getString("ownercf"), rs.getString("owneremail"));
        }

        return card;
    }


}

