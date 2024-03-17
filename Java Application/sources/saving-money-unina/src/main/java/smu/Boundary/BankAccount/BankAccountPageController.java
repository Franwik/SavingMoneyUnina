package smu.Boundary.BankAccount;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import smu.Boundary.ApplicationPageController;
import smu.Control.BankAccountControl;
import smu.DTO.BankAccount;
import smu.DTO.Person;

import java.io.IOException;

public class BankAccountPageController extends ApplicationPageController{

    @FXML
    private ComboBox<String> peopleChoser;

    @FXML
    private Text nameDisplay;

    @FXML
    private TableView<BankAccount> baList;

    @FXML
    private TableColumn<BankAccount, Integer> accountNumber;

    @FXML
    private TableColumn<BankAccount, Integer> balance;

    @FXML
    private TableColumn<BankAccount, String> bank;

    @FXML
    private void showNewBankAccountDialog() throws IOException {
        showDialog("NewBankAccountDialog", 370, 235, "Creazione Nuovo Conto Bancario", "BankAccount");
    }

    @FXML
    private void showDeleteBankAccountDialog() throws IOException {
        showDialog("DeleteBankAccountDialog", 370, 140, "Eliminazione Conto Bancario", "BankAccount");
    }

    @FXML
    private void showEditBankAccountDialog() throws IOException {
        showDialog("EditBankAccountDialog", 370, 280, "Modifica Conto Bancario", "BankAccount");
    }

    private void loadPeople(){

        List<String> people = BankAccountControl.getPeople();

        peopleChoser.getItems().add("---");
        peopleChoser.getItems().addAll(people);

    }

    @FXML
    private void loadBankAccounts(){

        List<BankAccount> bankAccounts = new ArrayList<>();
        String chosenPerson = peopleChoser.getSelectionModel().getSelectedItem().toString();

        baList.getItems().clear();

        if(chosenPerson.equals("---")){
            nameDisplay.setText("");
        }
        else {
            bankAccounts = BankAccountControl.getBankAccounts(chosenPerson);
            baList.getItems().addAll(bankAccounts);
            Person person = BankAccountControl.getPersonInfo(chosenPerson);
            nameDisplay.setText(person.getName() + " " + person.getSurname());
        }


    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Loads People in ComboBox
        loadPeople();

        //Initalize table columns
        accountNumber.setCellValueFactory(new PropertyValueFactory<BankAccount, Integer>("accountNumber"));
        balance.setCellValueFactory(new PropertyValueFactory<BankAccount, Integer>("balance"));
        bank.setCellValueFactory(new PropertyValueFactory<BankAccount, String>("bank"));

    }

}
