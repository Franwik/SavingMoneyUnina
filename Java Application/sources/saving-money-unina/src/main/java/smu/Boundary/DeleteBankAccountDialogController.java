package smu.Boundary;

import java.net.URL;
import java.io.*;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.App;
import smu.Control.BankAccountControl;

public class DeleteBankAccountDialogController extends BaseDialog {

    @FXML
    private ComboBox<Integer> baChoser;

    @FXML
    private void deleteBankAccount() throws IOException {

        //Fields from page
        Integer baID = baChoser.getSelectionModel().getSelectedItem();

        BankAccountControl.delete(baID);

        reload();
        loadBankAccounts();
    }

    private void reload(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteBankAccountDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 370, 140);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBankAccounts(){

        List<Integer> result = new ArrayList<>();
        
        result = BankAccountControl.getAllBankAccounts();

        baChoser.getItems().addAll(result);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        loadBankAccounts();
    }

}
