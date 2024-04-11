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
	private void updateWallet() throws IOException{
		//Fields from page

		Integer ID_Wallet = walletChooser.getSelectionModel().getSelectedItem();
		String walletCategory = walletCategoryField.getText();
		String walletName = walletNameField.getText();

		WalletControl.update(ID_Wallet, walletName, walletCategory);
	}

	private void loadWallets(){
		List<Integer> wallets = new ArrayList<>();

		wallets = WalletControl.getAllWallets();

		walletChooser.getItems().addAll(wallets);
	}

	@FXML
	private void loadWalletInfo(){
		Wallet wallet = null;

		wallet = WalletControl.getWalletInfo(walletChooser.getSelectionModel().getSelectedItem());

		walletCategoryField.setText(wallet.getWalletCategory());
		walletNameField.setText(wallet.getWalletName());

	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWallets();

	}

}
