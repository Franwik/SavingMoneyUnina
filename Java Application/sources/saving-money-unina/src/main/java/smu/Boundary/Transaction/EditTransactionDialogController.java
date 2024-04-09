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
	private ComboBox<String> InOutChooser;

	@FXML
	private DatePicker dateField;

	@FXML
	private ComboBox<String> categoryChooser;

	@FXML
	private ComboBox<String> walletChooser;

	@FXML
	private ComboBox<String> cardChooser;

	private String[] InOutTypes = {"Entrata", "Uscita"};

	@FXML
	private void updateTransaction() throws IOException{
		//Fields from page

		Integer transactionID = transactionChooser.getSelectionModel().getSelectedItem();
		String amount = amountField.getText();
		String InOut = InOutChooser.getSelectionModel().getSelectedItem();
		LocalDate date = dateField.getValue();
		String category = categoryChooser.getSelectionModel().getSelectedItem();
		String wallet = walletChooser.getSelectionModel().getSelectedItem();
		String cardNumber = cardChooser.getSelectionModel().getSelectedItem();

		TransactionControl.update(transactionID, amount, InOut, date, category, wallet, cardNumber);
		
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

		if (transaction.getAmount() > 0) {
			InOutChooser.setValue("Entrata");
		}
		else {
			InOutChooser.setValue("Uscita");
		}

		dateField.setValue(transaction.getDate());
		categoryChooser.setValue(transaction.getWalletCategory());
		walletChooser.setValue(transaction.getWalletName());
		cardChooser.setValue(transaction.getCardNumber());
		
	}

	private void loadCards(){
		cardChooser.getItems().clear();
		
		List<String> cards = new ArrayList<>();

		cards = TransactionControl.getCards();

		cardChooser.getItems().addAll(cards);
	}

	private void loadCategory(){
		categoryChooser.getItems().clear();

		List<String> categories = TransactionControl.getWalletCategory();
		List<String> uniqueCategories = new ArrayList<>(); 
	
		for (String category : categories) {
			if (!uniqueCategories.contains(category)) {
				uniqueCategories.add(category);
			}
		}

		categoryChooser.getItems().addAll(uniqueCategories);
	}

	@FXML
	private void loadWallet(){

		walletChooser.getItems().clear();
		List<String> wallets = new ArrayList<>();

		wallets = TransactionControl.getWalletNameByCategory(categoryChooser.getSelectionModel().getSelectedItem());

		walletChooser.getItems().addAll(wallets);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		InOutChooser.getItems().addAll(InOutTypes);
		
		loadCards();

		loadTransactions();

		loadCategory();
	}
	
}
