package smu.Control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.TransactionDAO;
import smu.DAO.WalletDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DAOImplementation.TransactionDAOimp;
import smu.DAOImplementation.WalletDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;
import smu.DTO.Transaction;
import smu.DTO.Wallet;


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

	public static List<String> getWalletNameByCategory(String category) {
		
		List<String> result = new ArrayList<>();
		List<Wallet> wallets = new ArrayList<>();

		LoggedUser loggedUser = LoggedUser.getInstance();

		WalletDAO walletDAO = new WalletDAOimp();

		try {
			wallets = walletDAO.getAllByEmailAndCategory(loggedUser.getEmail(), category);

			for(Wallet wallet : wallets){
				result.add(wallet.getWalletName());
			}
			
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		return result;

	}

	public static List<String> getWalletCategory(){

		List<String> result = new ArrayList<>();
		List<Wallet> category = new ArrayList<>();

		LoggedUser loggedUser = LoggedUser.getInstance();

		WalletDAO walletDAO = new WalletDAOimp();

		try {
			category = walletDAO.getAllByEmail(loggedUser.getEmail());

			for(Wallet wallet : category){
				
				String currentCategory = wallet.getWalletCategory();
				
				if (!result.contains(currentCategory)) {
					result.add(currentCategory);
				}

			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		return result;
	}

	public static List<String> getWalletName() {
		
		List<String> result = new ArrayList<>();
		List<Wallet> wallets = new ArrayList<>();

		LoggedUser loggedUser = LoggedUser.getInstance();

		WalletDAO walletDAO = new WalletDAOimp();

		try {
			wallets = walletDAO.getAllByEmail(loggedUser.getEmail());

			for(Wallet wallet : wallets){
				result.add(wallet.getWalletName());
			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		return result;

	}

	public static List<Transaction> getTransactions(String cardNumber) {
		List<Transaction> transactions = new ArrayList<>();
	
		TransactionDAO transactionDAO = new TransactionDAOimp();
	
		try {
			transactions = transactionDAO.getByCardNumber(cardNumber);
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
	

	public static void update(Integer transactionID, String amount, String InOut, LocalDate date, String category, String wallet, String cardNumber) {
		
		//DAO to interact with DB
		TransactionDAO transactionDAO = new TransactionDAOimp();

		//Transaction that needs to be inserted
		Transaction transaction = null;
		
		if(transactionID == null || amount == null || InOut == null || date == null || category == null || wallet == null || cardNumber == null){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
		}
		else{
			try {

				Float convertedAmount = Float.parseFloat(amount);

				if (convertedAmount != 0) {
					if (InOut.equals("Entrata") && convertedAmount < 0) {
	
						convertedAmount *= -1;
	
					}
					else if (InOut.equals("Uscita") && convertedAmount > 0) {
	
						convertedAmount *= -1;
	
					}
	
					transaction = new Transaction(transactionID, convertedAmount, date, category, wallet, cardNumber);
					transactionDAO.update(transaction);
					showAlert(AlertType.INFORMATION, "Informazione", "Transazione modificata con successo.", "La transazione è stata modificata con successo.");
				}
				else {
					showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La somma inserita non può essere uguale a 0.");
				}

			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
			} catch(RuntimeException e){
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La somma inserita non è valida.");
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

	public static void insert(String amount, String InOut, LocalDate date, String category, String walletName, String cardNumber) {
		
		//DAO to interact with DB
		TransactionDAO transactionDAO = new TransactionDAOimp();

		//Transaction that needs to be inserted
		Transaction transaction = null;
		
		if(amount == null || InOut == null || date == null || category == null || walletName == null || cardNumber == null){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
		}
		else{
			try {

				Float convertedAmount = Float.parseFloat(amount);

				if (convertedAmount != 0) {
					if (InOut.equals("Entrata") && convertedAmount < 0) {
	
						convertedAmount *= -1;
	
					}
					else if (InOut.equals("Uscita") && convertedAmount > 0) {
	
						convertedAmount *= -1;
	
					}
	
					transaction = new Transaction(convertedAmount, date, category, walletName, cardNumber);
					transactionDAO.insert(transaction);
					showAlert(AlertType.INFORMATION, "Informazione", "Transazione inserita con successo.", "La transazione è stata inserita con successo.");
				}
				else {
					showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La somma inserita non può essere uguale a 0.");
				}

			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
			} catch(RuntimeException e){
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La somma inserita non è valida.");
				System.err.println("Errore: " + e.getMessage());
			}
		}
	}
	
}
