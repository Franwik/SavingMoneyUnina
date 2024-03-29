package smu.Boundary.Transaction;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.TransactionControl;
import smu.DTO.Transaction;

public class EditTransactionDialogController extends BaseDialog{

	@FXML
	private ComboBox<Integer> transactionChooser;

	@FXML
	private TextField amountField;

	@FXML
	private DatePicker dateField;

	@FXML
	private ComboBox<String> categoryChooser;

	@FXML
	private ComboBox<String> walletChooser;

	@FXML
	private ComboBox<String> cardChooser;

	@FXML
	private void updateTransaction() throws IOException{
		//Fields from page
		Integer transactionID = transactionChooser.getSelectionModel().getSelectedItem();
		String amount = amountField.getText();
		LocalDate date = dateField.getValue();
		String category = categoryChooser.getSelectionModel().getSelectedItem();
		String wallet = walletChooser.getSelectionModel().getSelectedItem();
		String cardNumber = cardChooser.getSelectionModel().getSelectedItem();

		TransactionControl.update(transactionID, amount, date, category, wallet, cardNumber);
		
	}

	private void loadTransactions(){

		List<Integer> transactions = new ArrayList<>();

		transactions = TransactionControl.getAllTransactions();

		transactionChooser.getItems().addAll(transactions);

	}

	@FXML
	private void loadTransactionInfo(){
		Transaction transaction = TransactionControl.getTransactionInfo(transactionChooser.getSelectionModel().getSelectedItem());
		
		amountField.setText(String.valueOf(transaction.getAmount()));
		dateField.setValue(transaction.getDate());
		categoryChooser.setValue(transaction.getWalletCategory());
		walletChooser.setValue(transaction.getWalletName());
		cardChooser.setValue(transaction.getCardNumber());

		
	}

	private void loadCards(){
		List<String> cards = new ArrayList<>();

		cards = TransactionControl.getCards();

		cardChooser.getItems().addAll(cards);
	}

	private void loadWalletName(){
		List<String> wallets = new ArrayList<>();

		wallets = TransactionControl.getWalletName();

		//!!!
		walletChooser.getItems().addAll(wallets);
	}

	private void loadCategory(){
		List<String> categories = new ArrayList<>();

		categories = TransactionControl.getWalletCategory();

		categoryChooser.getItems().addAll(categories);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		loadCards();

		loadTransactions();

		loadWalletName();

		loadCategory();
	}
	
}
