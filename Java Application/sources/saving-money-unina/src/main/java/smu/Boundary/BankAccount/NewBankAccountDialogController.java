package smu.Boundary.BankAccount;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.BankAccountControl;

public class NewBankAccountDialogController extends BaseDialog {

    @FXML
    private TextField bankField;

    @FXML
    private TextField balanceField;

    @FXML
    private ComboBox<String> ownerChoser;

    @FXML
    private void createBankAccount() throws IOException {

        //Fields from page
        String bank = bankField.getText();
        String balance = balanceField.getText();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem();

        BankAccountControl.insert(bank, balance, ownerCF);

    }

    private void loadPeople(){

        List<String> people = new ArrayList<>();

        people = BankAccountControl.getPeople();

        ownerChoser.getItems().addAll(people);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadPeople();

    }

}
