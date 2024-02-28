package smu.Controller;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.LoggedUser;
import smu.DAO.BankAccountDAO;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.BankAccountDAOimp;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.BankAccount;
import smu.DTO.Card;
import smu.DTO.Familiar;

import java.util.*;
import java.sql.*;
import java.io.IOException;
import java.time.*;

public class NewCardDialogController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField ibanField;

    @FXML
    private TextField cvvField;

    @FXML
    private DatePicker expDField;

    @FXML
    private ComboBox<String> typeChoser;

    @FXML
    private ComboBox<Integer> baChoser;

    @FXML
    private ComboBox<String> ownerChoser;

    private String[] cardTypes = {"Prepagata", "Debito", "Credito"};

    @FXML
    private void createCard() throws IOException {
        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //instance of logged user
        LoggedUser loggedUser = LoggedUser.getInstance(null);

        //Card that needs to be inserted
        Card card = null;

        //Fields from page
        String cardNumber = cardNumberField.getText();
        String iban = ibanField.getText();
        String cvv = cvvField.getText();
        LocalDate expireDate = expDField.getValue();
        String type = typeChoser.getSelectionModel().getSelectedItem();
        Integer ba_number = baChoser.getSelectionModel().getSelectedItem();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem();

        //Aletrs to show in case of errors
        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("Almeno uno dei campi è vuoto.");

        Alert cardAdded = new Alert(AlertType.CONFIRMATION);
        cardAdded.setTitle("Successo");
        cardAdded.setContentText("Nuova carta inserita con successo.");

        //In case one of the field is empty
        if(cardNumber.isEmpty() || iban.isEmpty() || cvv.isEmpty() || expireDate == null || type == null || ba_number == null || ownerCF == null){
            emptyAlert.showAndWait();
        }
        else{

            try {

                if(ownerCF.equals(loggedUser.getCF())){
                    card = new Card(cardNumber, iban, cvv, expireDate, type, ba_number, null, loggedUser.getEmail());
                }
                else{
                    card = new Card(cardNumber, iban, cvv, expireDate, type, ba_number, ownerCF, null);
                }
                
                cardDAO.insert(card);

                cardAdded.showAndWait();


            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("codice: " + e.getSQLState());
            }

        }
    }

    @FXML
    private void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void loadPeople(){

        List<String> people = new ArrayList<>();
        List<Familiar> familiars = new ArrayList<>();

        //DAO to interact with DB
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        
        try {
            
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            people.add(loggedUser.getCF());

            for(Familiar familiar : familiars){
                people.add(familiar.getCF());
            }

            ownerChoser.getItems().addAll(people);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadBA(){

        List<Integer> result = new ArrayList<>();
        List<Familiar> familiars = new ArrayList<>();
        List<BankAccount> bankAccounts = new ArrayList<>();

        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        
        try {
            
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            bankAccounts.addAll(bankAccountDAO.getByEmail(loggedUser.getEmail()));

            for(Familiar familiar : familiars){
                bankAccounts.addAll(bankAccountDAO.getByCF(familiar.getCF()));
            }

            for(BankAccount bankAccount : bankAccounts){
                result.add(bankAccount.getAccountNumber());
            }

            baChoser.getItems().addAll(result);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        typeChoser.getItems().addAll(cardTypes);

        loadPeople();

        loadBA();
    }

}