package smu.Control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert.AlertType;
import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.TransactionDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DAOImplementation.TransactionDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;
import smu.DTO.Transaction;


public class TransactionControl extends BaseControl{

	public static List<String> getCards() {
		
		List<String> result = new ArrayList<>();
		List<Card> cards = new ArrayList<>();
		List<Familiar> familiars = new ArrayList<>();
	
		FamiliarDAO familiarDAO = new FamiliarDAOimp();
		CardDAO cardDAO = new CardDAOimp();

		LoggedUser loggedUser = LoggedUser.getInstance();

		
		try {
			cards.addAll(cardDAO.getByEmail(loggedUser.getEmail()));

			familiars.addAll(familiarDAO.getByEmail(loggedUser.getEmail()));

			for(Familiar familiar : familiars){
				cards.addAll(cardDAO.getByCF(familiar.getCF()));
			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		for(Card card : cards){
			result.add(card.getCardNumber());
		}

		return result;
	}

	public static List<Transaction> getTransactions(String choosenCard) {
		List<Transaction> transactions = new ArrayList<>();
	
		TransactionDAO transactionDAO = new TransactionDAOimp();
	
		try {
			transactions = transactionDAO.getByCardNumber(choosenCard);
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
	
		return transactions;
	}
	
	public static void update(Integer transactionID, String amount, LocalDate date, String category, String wallet, String cardNumber) {
		TransactionDAO transactionDAO = new TransactionDAOimp();
		Transaction transaction = new Transaction(transactionID, Float.valueOf(amount), date, category, wallet, cardNumber);
		
		try {
			transactionDAO.update(transaction);
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
	}

	public static Transaction getTransactions(Integer selectedItem) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTransactions'");
	}

}
