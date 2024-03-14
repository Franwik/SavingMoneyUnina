package smu.Control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

	public static Transaction getTransactionInfo(Integer ID_Transaction) {
		TransactionDAO transactionDAO = new TransactionDAOimp();
		Transaction transaction = null;
		
		try {
			transaction = transactionDAO.getById(ID_Transaction);
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
		
		return transaction;
	}
	
	public static List<Integer> getAllTransactions() {

		List<Integer> result = new ArrayList<>();
		List<Card> cards = new ArrayList<>();
		List<Transaction> transactions = new ArrayList<>();
		List<Familiar> familiars = new ArrayList<>();
	
		TransactionDAO transactionDAO = new TransactionDAOimp();
		CardDAO cardDAO = new CardDAOimp();
		FamiliarDAO familiarDAO = new FamiliarDAOimp();
	
		LoggedUser loggedUser = LoggedUser.getInstance();
	
		try {
			// Recupero delle carte dell'utente loggato
			cards.addAll(cardDAO.getByEmail(loggedUser.getEmail()));
	
			// Recupero dei familiari associati all'utente loggato
			familiars.addAll(familiarDAO.getByEmail(loggedUser.getEmail()));
	
			// Recupero delle carte dei familiari e delle loro transazioni
			for (Familiar familiar : familiars) {
				cards.addAll(cardDAO.getByCF(familiar.getCF()));
			}
	
			// Recupero delle transazioni associate alle carte
			for (Card card : cards) {
				transactions.addAll(transactionDAO.getByCardNumber(card.getCardNumber()));
			}
	
			for (Transaction transaction : transactions) {
				result.add(transaction.getID_Transaction());
			}
	
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
	
		return result;
	}
	

	public static void update(Integer transactionID, String amount, LocalDate date, String category, String wallet, String cardNumber) {
		
		//DAO to interact with DB
		TransactionDAO transactionDAO = new TransactionDAOimp();

		//Card that needs to be inserted
		Transaction transaction = null;

		//checks on input
		if(transactionID == null || amount.isEmpty() || date == null || cardNumber == null){

		   showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
		
	   }
	   else{

			try{
			
					transaction = new Transaction(transactionID, Float.parseFloat(amount), date, category, wallet, cardNumber);
				   	transactionDAO.update(transaction);
				   	showAlert(AlertType.INFORMATION, "Informazione", "Transazione modificata con successo.", "La transazione è stata modificata con successo.");
			   
			   } catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
				System.err.println("Errore: " + e.getMessage());
			}

		}
	}

	public static void delete(Integer transactionID) {

		//DAO to interact with DB
		TransactionDAO transactionDAO = new TransactionDAOimp();

		
		if(transactionID == null){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Nessuna transazione selezionata.");
		}
		else{

			try{

				Optional<ButtonType> choice = showAlert(AlertType.CONFIRMATION, "Attenzione", "Vuoi procedere?", "Sei sicuro di voler eliminare la transazione " + transactionID.intValue() + "?");

				if(choice.get() == ButtonType.OK){
					transactionDAO.delete(transactionID);
					showAlert(AlertType.INFORMATION, "Informazione", "Transazione eliminata con successo.", "La transazione è stata eliminata con successo.");
				}
				
			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
				System.err.println("Errore: " + e.getMessage());
			}
		}

	}

	public static void insert(Integer iD_Transaction, Float amount, LocalDate date, String category, String walletName, String cardNumber) {
		
		//DAO to interact with DB
		TransactionDAO transactionDAO = new TransactionDAOimp();

		//Transaction that needs to be inserted
		Transaction transaction = null;
		

		if(iD_Transaction == null || amount == null || date == null || cardNumber.isEmpty()){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
		}
		else{
			try {

				transaction = new Transaction(iD_Transaction, amount, date, category, walletName, cardNumber);
				transactionDAO.insert(transaction);
				showAlert(AlertType.INFORMATION, "Informazione", "Transazione inserita con successo.", "La transazione è stata inserita con successo.");

			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
			} catch(RuntimeException e){
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il saldo inserito non è valido.");
				System.err.println("Errore: " + e.getMessage());
			}
		}
	}
}
