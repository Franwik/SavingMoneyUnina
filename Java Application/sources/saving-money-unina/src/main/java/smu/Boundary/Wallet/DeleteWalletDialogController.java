package smu.Boundary.Wallet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.testapps.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.Boundary.BaseDialog;
import smu.Control.WalletControl;

public class DeleteWalletDialogController extends BaseDialog{

	@FXML
	private ComboBox<Integer> walletChooser;



	@FXML
	private void deleteWallet() {
		Integer walletID = walletChooser.getSelectionModel().getSelectedIndex();

		WalletControl.delete(walletID);

		reload();
		loadWallets();
	}

	@FXML
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

	@FXML
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
