package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.DAO.TransactionDAO;
import smu.Database;
import smu.DTO.Transaction;

public class TransactionDAOimp implements TransactionDAO {

    @Override
    public Transaction getById(int id) throws SQLException {

        Connection con = Database.getConnection();
        
		Transaction transaction = null;

        String sql = "SELECT * FROM transaction WHERE ID_Transaction = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);
        
		ResultSet rs = ps.executeQuery();
		
		if (rs.next())
            transaction = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date").toLocalDate(), rs.getString("category"), rs.getString("walletName"), rs.getString("cardNumber"));
        

        return transaction;
    }

	@Override
	public List<Transaction> getByCardNumber(String chosenCard) throws SQLException{
		Connection con = Database.getConnection();
        List<Transaction> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.transaction WHERE cardnumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, chosenCard);
        
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
           Transaction transactions = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date").toLocalDate(), rs.getString("category"), rs.getString("walletName"), rs.getString("cardNumber"));
		   result.add(transactions);
		}
        
		return result;

	}

	public int insert(Transaction transaction) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "INSERT INTO smu.transaction (amount, Date, category, walletname, cardnumber) VALUES (?, ?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setFloat(1, transaction.getAmount());
		ps.setDate(2, java.sql.Date.valueOf(transaction.getDate()));
		ps.setString(3, transaction.getCategory());
		ps.setString(4, transaction.getWalletName());
		ps.setString(5, transaction.getCardNumber());

		int result = ps.executeUpdate();

		return result;
	}

	@Override
	public int update(Transaction transaction) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "UPDATE smu.transaction SET amount = ?, Date = ?, category = ?, walletname = ?, cardnumber = ? WHERE ID_Transaction = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setFloat(1, transaction.getAmount());
		ps.setDate(2, java.sql.Date.valueOf(transaction.getDate()));
		ps.setString(3, transaction.getCategory());
		ps.setString(4, transaction.getWalletName());
		ps.setString(5, transaction.getCardNumber());
		ps.setInt(6, transaction.getID_Transaction());

		int result = ps.executeUpdate();

		return result;
	}

	@Override
	public int delete(int id) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "DELETE FROM smu.transaction WHERE ID_Transaction = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setInt(1, id);

		int result = ps.executeUpdate();

		return result;
	}

	
}