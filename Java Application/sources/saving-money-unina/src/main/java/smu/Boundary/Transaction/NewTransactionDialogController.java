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
	private TextField categoryField;

	@FXML
	private TextField walletNameField;

	@FXML
	private ComboBox<String> cardChooser;


	@FXML
	private void createTransaction() {

		Integer ID_Transaction = Integer.parseInt(ID_TransactionField.getText());
		Float amount = Float.parseFloat(amountField.getText());
		LocalDate date = dateField.getValue();
		String category = categoryField.getText();
		String walletName = walletNameField.getText();
		String cardNumber = cardChooser.getSelectionModel().getSelectedItem();

		TransactionControl.insert(ID_Transaction, amount, date, category, walletName, cardNumber);
		
	}

	private void loadCards(){

		List<String> cards = new ArrayList<>();

		cards = TransactionControl.getCards();

		cardChooser.getItems().addAll(cards);

	}

	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		loadCards();


	}
	
}
