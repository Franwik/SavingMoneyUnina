package smu.Boundary.Wallet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.App;
import smu.Boundary.BaseDialog;
import smu.Control.WalletControl;

public class DeleteWalletDialogController extends BaseDialog{

	@FXML
	private ComboBox<Integer> walletChooser;

	@FXML
	private void deleteWallet() throws IOException{
		Integer walletID = walletChooser.getSelectionModel().getSelectedItem();

		WalletControl.delete(walletID);

		reload();
		loadWallets();
		
	}


	private void reload(){
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteWalletDialog.fxml"));
		Scene scene;

		try {

			scene = new Scene(fxmlLoader.load(), 370, 140);
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadWallets(){
		
		List<Integer> result = new ArrayList<>();

		result = WalletControl.getAllWallets();

		walletChooser.getItems().addAll(result);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadWallets();

	}
	
}
