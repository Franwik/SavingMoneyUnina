package smu.Boundary.Transaction;

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

public class NewTransactionDialogController extends BaseDialog{

	@FXML
	private TextField ID_TransactionField;

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
	private void createTransaction() {

		Integer ID_Transaction = Integer.parseInt(ID_TransactionField.getText());
		Float amount = Float.parseFloat(amountField.getText());
		LocalDate date = dateField.getValue();
		String category = categoryChooser.getSelectionModel().getSelectedItem();
		String walletName = walletChooser.getSelectionModel().getSelectedItem();;
		String cardNumber = cardChooser.getSelectionModel().getSelectedItem();

		TransactionControl.insert(ID_Transaction, amount, date, category, walletName, cardNumber);
		
	}

	private void loadCards(){

		List<String> cards = new ArrayList<>();

		cards = TransactionControl.getCards();

		cardChooser.getItems().addAll(cards);

	}

	private void loadWallet(){

		List<String> wallets = new ArrayList<>();

		wallets = TransactionControl.getWalletName();

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

		loadWallet();

		loadCategory();

	}
	
}
