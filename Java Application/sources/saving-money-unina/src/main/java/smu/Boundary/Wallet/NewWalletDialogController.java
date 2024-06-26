package smu.Boundary.Wallet;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.TransactionControl;
import smu.Control.WalletControl;

public class NewWalletDialogController extends BaseDialog{

	@FXML
	private ComboBox<String> walletChooser;

	@FXML
	private TextField walletCategoryField;

	@FXML
	private TextField walletNameField;

	@FXML
	private void createWallet(){
		//Fields from page
		String walletCategory = null;
		String walletName = walletNameField.getText();

		if(walletChooser.getValue() != null){
			if (walletChooser.getValue().equals("Inserisci nuova categoria")) {
				if (!walletCategoryField.getText().isEmpty()){
					walletCategory = walletCategoryField.getText();
				}
			} else {
				
				walletCategory = walletChooser.getSelectionModel().getSelectedItem();
			}
			
		}
		WalletControl.insert(walletName, walletCategory);
	}

	private void loadWallet(){
		walletChooser.getItems().clear();

		List<String> wallets = TransactionControl.getWalletCategory();

		walletChooser.getItems().add("---");
		walletChooser.getItems().add("Inserisci nuova categoria");
		walletChooser.getItems().addAll(wallets);
	}

	@FXML
	private void checkCategoryField(){

		if(walletChooser.getValue().equals("Inserisci nuova categoria")){
			walletCategoryField.setDisable(false);
		}
		else{
			walletChooser.setDisable(false);
			walletCategoryField.setDisable(true);
			walletCategoryField.clear();
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadWallet();

	}

}


