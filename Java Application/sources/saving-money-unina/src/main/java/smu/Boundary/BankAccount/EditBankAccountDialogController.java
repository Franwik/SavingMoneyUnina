package smu.Boundary.BankAccount;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.BankAccountControl;
import smu.Control.CardControl;
import smu.DTO.BankAccount;

public class EditBankAccountDialogController extends BaseDialog {

    @FXML
    private ComboBox<Integer> baChoser;

    @FXML
    private TextField bankField;

    @FXML
    private TextField balanceField;

    @FXML
    private ComboBox<String> ownerChoser;

    @FXML
    private void updateBankAccount() throws IOException {

        //Fields from page
        
        Integer baNumber = baChoser.getSelectionModel().getSelectedItem();
        String bankName = bankField.getText();
        String balance = balanceField.getText();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem();

        BankAccountControl.update(baNumber, bankName, balance, ownerCF);

    }

    @FXML
    private void loadBankAccountInfo(){

        BankAccount bankAccount = BankAccountControl.getBankAccountInfo(baChoser.getSelectionModel().getSelectedItem());

        bankField.setText(bankAccount.getBank());
        balanceField.setText(bankAccount.getBalance().toString());
        ownerChoser.setValue(BankAccountControl.getBankAccountOwnerCF(bankAccount.getAccountNumber()));
    }

    private void loadPeople(){

        List<String> people = new ArrayList<>();

        people = CardControl.getPeople();

        ownerChoser.getItems().addAll(people);

    }

    private void loadBA(){

        List<Integer> bankAccounts = new ArrayList<>();

        bankAccounts = CardControl.getAllBA();

        baChoser.getItems().addAll(bankAccounts);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadPeople();

        loadBA();

    }

}
