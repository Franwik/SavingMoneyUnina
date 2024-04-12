package smu.Control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import smu.LoggedUser;
import smu.DAO.FamiliarDAO;
import smu.DAO.TransactionDAO;
import smu.DAO.WalletDAO;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DAOImplementation.TransactionDAOimp;
import smu.DAOImplementation.WalletDAOimp;
import smu.DTO.Familiar;
import smu.DTO.Transaction;
import smu.DTO.Wallet;

public class WalletControl extends BaseControl{

	public static List<String> getWallets(){

		List<String> result = new ArrayList<>();
		List<Wallet> wallets = new ArrayList<>();
		List<Familiar> familiars = new ArrayList<>();

		FamiliarDAO familiarDAO = new FamiliarDAOimp();
		WalletDAO walletDAO = new WalletDAOimp();

		LoggedUser loggedUser = LoggedUser.getInstance();

		try{
			wallets.addAll(walletDAO.getAllByEmail(loggedUser.getEmail()));

			familiars.addAll(familiarDAO.getByEmail(loggedUser.getEmail()));

			for(Familiar familiar : familiars){
				wallets.addAll(walletDAO.getAllByEmail(familiar.getCF()));
			}
		} catch (SQLException e){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		for(Wallet wallet : wallets){
			result.add(wallet.getWalletCategory());
		}

		return result;

	}

	public static Wallet getWalletInfo(Integer ID_Wallet) {
		
		WalletDAO walletDAO = new WalletDAOimp();

		Wallet wallet = null;
		
		try {

			wallet = walletDAO.getById(ID_Wallet);

		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
		
		return wallet;
	}

	public static Wallet getWalletInfoByName(String walletName) {
		WalletDAO walletDAO = new WalletDAOimp();
		Wallet wallet = null;
		try {
			wallet = walletDAO.getByName(walletName);
			// Recupera anche il saldo del portafoglio
			if (wallet != null) {
				float totalAmount = walletDAO.getTotalAmountById(wallet.getId_wallet());
				wallet.setTotalAmount(totalAmount);
			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
		return wallet;
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
	
	


	public static List<Transaction> getTransaction(String choosenWallet){

		List<Transaction> transactions = new ArrayList<>();

		TransactionDAO transactionDAO = new TransactionDAOimp();

		try{
			transactions = transactionDAO.getByWalletCategory(choosenWallet);
		} catch (SQLException e){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		return transactions;
			
	}

	public static List<Transaction> getTransactionWallet(String choosenWalletName, String category) {
		
		List<Transaction> transactions = new ArrayList<>();
		
		TransactionDAO transactionDAO = new TransactionDAOimp();
		
		try {
			transactions = transactionDAO.getByWalletName(choosenWalletName, category);
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}
		
		return transactions;
	}

	

	public static Transaction getTransactionInfo(Integer ID_Transaction){
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
	
	public static List<Integer> getAllTransaction() {

		List<Integer> result = new ArrayList<>();
		List<Transaction> transactions = new ArrayList<>();
		List<Wallet> wallets = new ArrayList<>();
	
		WalletDAO walletDAO = new WalletDAOimp();
	
		LoggedUser loggedUser = LoggedUser.getInstance();
	
		try {
	
			// Recupera tutti i portafogli dell'utente loggato
			wallets.addAll(walletDAO.getAllByEmail(loggedUser.getEmail()));
	
			// Itera sui portafogli e recupera le transazioni per ciascun portafoglio
			for (Wallet wallet : wallets) {
				// Recupera le transazioni per il portafoglio corrente
				transactions.addAll(TransactionControl.getTransactions(wallet.getWalletCategory()));
			}
	
			// Aggiungi gli ID delle transazioni alla lista dei risultati
			for (Transaction transaction : transactions) {
				result.add(transaction.getID_Transaction());
			}
	
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
	
		return result;
	}
	

	public static List<Integer> getAllWallets() {
		
		List<Integer> result = new ArrayList<>();
		List<Wallet> wallets = new ArrayList<>();
		
		//DAO to interact with DB
		WalletDAO walletDAO = new WalletDAOimp();
		
		//Current user
		LoggedUser loggedUser = LoggedUser.getInstance();
		
		try {

			wallets.addAll(walletDAO.getAllByEmail(loggedUser.getEmail()));
			
			for(Wallet wallet : wallets){
				result.add(wallet.getId_wallet());
			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
			System.err.println("Errore: " + e.getMessage());
		}
		
		return result;
	}



	public static void update(Integer ID_Wallet, String walletName, String walletCategory){

		WalletDAO walletDAO = new WalletDAOimp();

		Wallet wallet = null;

		if(ID_Wallet == null || walletCategory == null){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
		}
		else{
			try {

				wallet = new Wallet(ID_Wallet, walletName, walletCategory);
				walletDAO.update(wallet);
				showAlert(AlertType.INFORMATION, "Informazione", "Portafoglio aggiornato con successo.", "Il portafoglio è stato aggiornato con successo.");
			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
				System.err.println("Errore: " + e.getMessage());
			} catch (RuntimeException e){
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La somma inserita non è valida.");
				System.err.println("Errore: " + e.getMessage());
			}
		}
	}

	public static void delete(Integer ID_Wallet){

		WalletDAO walletDAO = new WalletDAOimp();
		
		if(ID_Wallet == null){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Nessuna transazione selezionata.");
		}
		else{
			try {
				Optional<ButtonType> choice = showAlert(AlertType.CONFIRMATION, "Attenzione", "Sei sicuro di voler eliminare il portafoglio?", "Sei sicuro di voler eliminare il portafoglio " + ID_Wallet.intValue() + "?");
				if(choice.get() == ButtonType.OK){
					walletDAO.delete(ID_Wallet);
					showAlert(AlertType.INFORMATION, "Informazione", "Portafoglio eliminato con successo.", "");
				}
			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
				System.err.println("Errore: " + e.getMessage());
			}
		}
	}

	public static void insert(String walletName, String walletCategory){

		WalletDAO walletDAO = new WalletDAOimp();

		Wallet wallet = null;

		if(walletCategory == null || walletCategory.isEmpty() || walletCategory.equals("---")){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
			return;
		}
		else if(!walletCategory.isEmpty() && walletName.isEmpty()){
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Scegli il nome del nuovo portafoglio.");
			return;
		}
		else{
			try {

				wallet = new Wallet(walletName, walletCategory);
				walletDAO.insert(wallet);
				showAlert(AlertType.INFORMATION, "Informazione", "Portafoglio inserito con successo.", "");
			
			} catch (SQLException e) {
			
				showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
				System.err.println("Errore: " + e.getMessage());
			
			}
		}


	}

}
