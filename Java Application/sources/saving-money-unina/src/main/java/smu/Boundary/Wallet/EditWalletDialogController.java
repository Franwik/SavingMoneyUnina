package smu.Boundary.Wallet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.WalletControl;
import smu.DTO.Wallet;

public class EditWalletDialogController extends BaseDialog{

	@FXML
	private ComboBox<Integer> walletChooser;

	@FXML
	private TextField walletCategoryField;

	@FXML
	private TextField walletNameField;

	@FXML
	private TextField totalAmountField;

	@FXML
	private ComboBox<String> emailChooser;

	@FXML
	private void updateWallet() throws IOException{
		//Fields from page

		Integer ID_Wallet = walletChooser.getSelectionModel().getSelectedItem();
		String walletCategory = walletCategoryField.getText();
		String walletName = walletNameField.getText();
		String totalAmount = totalAmountField.getText();
		String email = emailChooser.getSelectionModel().getSelectedItem();

		WalletControl.update(ID_Wallet, walletCategory, walletName, totalAmount, email);
	}

	private void loadWallets(){
		List<Integer> wallets = new ArrayList<>();

		wallets = WalletControl.getAllWallets();

		walletChooser.getItems().addAll(wallets);
	}

	private void loadWalletInfo(){
		Wallet wallet = null;

		wallet = WalletControl.getWalletInfo(walletChooser.getSelectionModel().getSelectedItem());

		walletCategoryField.setText(wallet.getWalletCategory());
		walletNameField.setText(wallet.getWalletName());
		totalAmountField.setText(String.valueOf(wallet.getTotalAmount()));
		emailChooser.setValue(wallet.getOwnerEmail());

	}

	private void loadOwnerEmails(){
		List<String> emails = new ArrayList<>();

		emails = WalletControl.getOwnerEmail();

		emailChooser.getItems().addAll(emails);
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWallets();
		loadOwnerEmails();
		loadWalletInfo();
	}

}
