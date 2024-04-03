package smu.Boundary.Wallet;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.WalletControl;

public class NewWalletDialogController extends BaseDialog{

	@FXML
	private TextField walletCategoryField;

	@FXML
	private TextField walletNameField;

	@FXML
	private TextField totalAmountField;

	@FXML
	private ComboBox<String> emailChooser;

	@FXML
	private void createWallet(){
		//Fields from page

		String walletCategory = walletCategoryField.getText();
		String walletName = walletNameField.getText();
		String totalAmount = totalAmountField.getText();
		String email = emailChooser.getSelectionModel().getSelectedItem();

		WalletControl.insert(walletCategory, walletName, totalAmount, email);
	
	}

	private void loadEmails(){

		List<String> emails = new ArrayList<>();

		emails = WalletControl.getOwnerEmail();

		emailChooser.getItems().addAll(emails);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadEmails();

	}

}
