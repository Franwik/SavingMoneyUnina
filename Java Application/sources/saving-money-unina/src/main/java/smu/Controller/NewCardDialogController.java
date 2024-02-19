package smu.Controller;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import smu.App;
import smu.LoggedUser;
import smu.DAO.BankAccountDAO;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAO.UserDAO;
import smu.DAOImplementation.BankAccountDAOimp;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DAOImplementation.UserDAOimp;
import smu.DTO.BankAccount;
import smu.DTO.Familiar;
import java.util.*;
import java.sql.*;
import java.io.IOException;
import java.time.*;

public class NewCardDialogController implements Initializable {

    @FXML
    private TextField ibanField;

    @FXML
    private TextField cvvField;

    @FXML
    private DatePicker dobField;

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
        CardDAO userDAO = new CardDAOimp();

        //Singleton of logged user
        LoggedUser loggedUser = null;

        //Fields from page
        String iban = ibanField.getText();
        String cvv = cvvField.getText();
        LocalDate dob = dobField.getValue();
        String type = typeChoser.getSelectionModel().getSelectedItem().toString();
        Integer ba_number = baChoser.getSelectionModel().getSelectedItem();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem().toString();

        //Aletrs to show in case of errors
        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("Almeno uno dei campi è vuoto.");

        Alert wrongLogin = new Alert(AlertType.CONFIRMATION);
        wrongLogin.setTitle("Successo");
        wrongLogin.setContentText("Nuova carta inserita con successo.");

        //In case one of the field is empty
        if(iban.isEmpty() || cvv.isEmpty() || dob == null || type.isEmpty() || ba_number == null || ownerCF.isEmpty()){
            emptyAlert.showAndWait();
        }
        else{

            try {

                System.out.println(type + " " + ba_number + " " + ownerCF);

                loggedUser = LoggedUser.getInstance(null);

                //if(loggedUser != null){
                    App.setRoot("Home");
                //}
                //else{
                //    wrongLogin.showAndWait();
                //}
            //} catch (SQLException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
    public void initialize(URL location, ResourceBundle resources) {

        typeChoser.getItems().addAll(cardTypes);

        loadPeople();

        loadBA();
        
    }

}