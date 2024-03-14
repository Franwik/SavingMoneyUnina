package smu.Boundary;

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
import smu.Control.TransactionControl;

public class DeleteTransactionDialogController extends BaseDialog{

	@FXML
	private ComboBox<Integer> transactionChooser;

	@FXML
	private void deleteTransaction() throws IOException{

		Integer transactionID = transactionChooser.getSelectionModel().getSelectedItem();

		TransactionControl.delete(transactionID);

		reload();
		loadTransactions();

		
	}

	private void reload(){
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteTransactionDialog.fxml"));
		Scene scene;

		try {

			scene = new Scene(fxmlLoader.load(), 370, 140);
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTransactions(){

		List<Integer> result = new ArrayList<>();

		result = TransactionControl.getAllTransactions();


		transactionChooser.getItems().addAll(result);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadTransactions();

	}

	
}
