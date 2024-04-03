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
	private void createTransaction() {
		//Fields from page

		String amount = amountField.getText();
		String InOut = InOutChooser.getSelectionModel().getSelectedItem();
		LocalDate date = dateField.getValue();
		String category = categoryChooser.getSelectionModel().getSelectedItem();
		String walletName = walletChooser.getSelectionModel().getSelectedItem();
		String cardNumber = cardChooser.getSelectionModel().getSelectedItem();

		TransactionControl.insert(amount, InOut, date, category, walletName, cardNumber);
		
	}

	private void loadCards(){

		List<String> cards = new ArrayList<>();

		cards = TransactionControl.getCards();

		cardChooser.getItems().addAll(cards);

	}

	private void loadCategory(){
		
		List<String> categories = new ArrayList<>();

		categories = TransactionControl.getWalletCategory();

		categoryChooser.getItems().addAll(categories);

	}

	@FXML
	private void loadWallet(){

		List<String> wallets = new ArrayList<>();

		wallets = TransactionControl.getWalletNameByCategory(categoryChooser.getSelectionModel().getSelectedItem());

		walletChooser.getItems().addAll(wallets);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		InOutChooser.getItems().addAll(InOutTypes);

		loadCards();

		loadCategory();

	}
	
}
