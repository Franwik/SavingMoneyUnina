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
            transaction = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date"), rs.getString("category"));
        

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
				transaction = new Transaction(rs.getInt("ID_Transaction"), rs.getFloat("amount"), rs.getDate("Date"), rs.getString("category"));
			}
			transactions.add(transaction);
		}	
		return transactions;
	}

}
