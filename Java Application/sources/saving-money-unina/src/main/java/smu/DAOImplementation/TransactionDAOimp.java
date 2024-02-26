package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAO.TransactionDAO;
import smu.Database;
import smu.LoggedUser;
import smu.DTO.Card;
import smu.DTO.Familiar;
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
            transaction = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date").toLocalDate(), rs.getString("category"), rs.getString("cardiban"));
        

        return transaction;
    }

    @Override
    public List<Transaction> getAllByEmail(String email) throws SQLException {
        
		Connection con = Database.getConnection();

		FamiliarDAO familiarDAO = new FamiliarDAOimp();
		CardDAO cardDAO = new CardDAOimp();
		
		List<Transaction> transactions = new ArrayList<>();

		List<Familiar> familiars = new ArrayList<>();

		List<Card> cards = new ArrayList<>();

		LoggedUser loggedUser = LoggedUser.getInstance(null);

		familiars = familiarDAO.getByEmail(loggedUser.getEmail());

		cards.addAll(cardDAO.getByEmail(loggedUser.getEmail()));

		for(Familiar familiar : familiars){
			cards.addAll(cardDAO.getByCF(familiar.getCF()));
		}

		String sql = "SELECT * FROM transaction WHERE cardiban = ?";

		for(Card card : cards){
			Transaction transaction = null;
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, card.getIban());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				transaction = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date").toLocalDate(), rs.getString("category"), rs.getString("cardiban"));
			}
			transactions.add(transaction);
		}	
		return transactions;
	}

	public int insert(Transaction transaction) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "INSERT INTO transaction (amount, Date, category, cardiban) VALUES (?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setFloat(1, transaction.getAmount());
		ps.setDate(2, java.sql.Date.valueOf(transaction.getDate()));
		ps.setString(3, transaction.getCategory());
		ps.setString(4, transaction.getCardiban());

		int result = ps.executeUpdate();

		return result;
	}

	@Override
	public int update(Transaction transaction) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "UPDATE transaction SET amount = ?, Date = ?, category = ?, cardiban = ? WHERE ID_Transaction = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setFloat(1, transaction.getAmount());
		ps.setDate(2, java.sql.Date.valueOf(transaction.getDate()));
		ps.setString(3, transaction.getCategory());
		ps.setString(4, transaction.getCardiban());
		ps.setInt(5, transaction.getID_Transaction());

		int result = ps.executeUpdate();

		return result;
	}

	@Override
	public int delete(int id) throws SQLException {
		Connection con = Database.getConnection();

		String sql = "DELETE FROM transaction WHERE ID_Transaction = ? CASCADE";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setInt(1, id);

		int result = ps.executeUpdate();

		return result;
	}
	

}
