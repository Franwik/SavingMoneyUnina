package smu.Control;

import java.util.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.sql.*;

import smu.LoggedUser;
import smu.DAO.BankAccountDAO;
import smu.DAO.FamiliarDAO;
import smu.DAO.UserDAO;
import smu.DAOImplementation.BankAccountDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DAOImplementation.UserDAOimp;
import smu.DTO.BankAccount;
import smu.DTO.Familiar;
import smu.DTO.Person;

public class BankAccountControl extends BaseControl {

    public static List<String> getPeople(){

        List<String> peopleCFList = new ArrayList<>();

        //DAO to interact with DB
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        try {
            
            List<Familiar> familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            peopleCFList.add(loggedUser.getCF());

            for(Familiar familiar : familiars){
                peopleCFList.add(familiar.getCF());
            }
            
        } catch (SQLException e) {
            System.err.println("Errore: " + e.getMessage());
        }

        return peopleCFList;
    }

    public static List<BankAccount> getBankAccounts(String chosenPerson) {

        List<BankAccount> bankAccounts = new ArrayList<>();

        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        try {
            
            if (chosenPerson.equals(loggedUser.getCF())) {
                bankAccounts = bankAccountDAO.getByEmail(loggedUser.getEmail());
            }
            else {
                bankAccounts = bankAccountDAO.getByCF(chosenPerson);
            }
            
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            System.err.println("Errore: " + e.getMessage());
        }

        return bankAccounts;
    }

    public static Person getPersonInfo(String CF){
        
        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        if (CF.equals(loggedUser.getCF())) {
            return loggedUser;
        }
        else {

            Familiar familiar = null;

            try {

                FamiliarDAO familiarDAO = new FamiliarDAOimp();
                familiar = familiarDAO.getByCF(CF);

            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
            }
            
            return familiar;
        }
        
    }
    
    public static List<Integer> getAllBankAccounts() {
        List<Integer> result = new ArrayList<>();
        List<Familiar> familiars = new ArrayList<>();
        List<BankAccount> bankAccounts = new ArrayList<>();

        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        try {
            
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            bankAccounts.addAll(bankAccountDAO.getByEmail(loggedUser.getEmail()));

            for(Familiar familiar : familiars){
                bankAccounts.addAll(bankAccountDAO.getByCF(familiar.getCF()));
            }

            for(BankAccount bankAccount : bankAccounts){
                result.add(bankAccount.getAccountNumber());
            }
            
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            System.err.println("Errore: " + e.getMessage());
        }

        return result;
    }

    public static void insert(String bank, String balance, String ownerCF) {
        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();

        //instance of logged user
        LoggedUser loggedUser = LoggedUser.getInstance();

        //BankAccount that needs to be inserted
        BankAccount bankAccount = null;

        //checks on input
        if(bank.isEmpty() || balance == null ||  ownerCF == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        else{

            try {

                if(ownerCF.equals(loggedUser.getCF())){
                    bankAccount = new BankAccount(Integer.valueOf(balance), null, bank, loggedUser.getEmail(), null);
                }
                else{
                    bankAccount = new BankAccount(Integer.valueOf(balance), null, bank, null, ownerCF);
                }
                
                bankAccountDAO.insert(bankAccount);

                showAlert(AlertType.INFORMATION, "Successo", "Nuovo conto bancario inserita con successo.", "Nuovo conto bancario inserita con successo.");


            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
            } catch (RuntimeException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il saldo inserito non è valido.");
                System.err.println("Errore: " + e.getMessage());
            }

        }
    }
    
    public static void delete(Integer baID) {

        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();

        //In case one of the field is empty
        if(baID == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Non è stata selezionata nessun conto bancario.");
        }
        else{

            try {

                Optional<ButtonType> choice = showAlert(AlertType.CONFIRMATION, "Attenzione", "Vuoi procedere?", "Sei sicuro di voler eliminare la carta " + baID.toString() + "?");

                if(choice.get() == ButtonType.OK){
                    bankAccountDAO.delete(baID);
                }


            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
            }

        }

    }

    public static BankAccount getBankAccountInfo(Integer baNumber) {
        BankAccount bankAccount = null;
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();

        try {
            bankAccount = bankAccountDAO.getByID(baNumber);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            System.err.println("Errore: " + e.getMessage());
        }

        return bankAccount;
    }

    public static String getBankAccountOwnerCF(Integer baNumber) {
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();
        UserDAO userDAO = new UserDAOimp();
        BankAccount bankAccount = null;
        String CF = null;

        try {
            bankAccount = bankAccountDAO.getByID(baNumber);
            if (bankAccount.getOwnerCF() != null) {
                CF = bankAccount.getOwnerCF();
            }
            else {
                CF = userDAO.getByEmail(bankAccount.getOwnerEmail()).getCF();
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            System.err.println("Errore: " + e.getMessage());
        }

        return CF;
    }

    public static void update(Integer baNumber, String bankName, String balance, String ownerCF) {
        //DAO to interact with DB
        BankAccountDAO bankAccountDAO = new BankAccountDAOimp();

        //instance of logged user
        LoggedUser loggedUser = LoggedUser.getInstance();

        //BankAccount that needs to be updated
        BankAccount bankAccount = null;

        //checks on input
        if(baNumber == null || bankName.isEmpty() || balance.isEmpty() || ownerCF == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        else{

            try {

                if(ownerCF.equals(loggedUser.getCF())){
                    bankAccount = new BankAccount(Integer.valueOf(balance), baNumber, bankName, loggedUser.getEmail(), null);
                }
                else{
                    bankAccount = new BankAccount(Integer.valueOf(balance), baNumber, bankName, null, ownerCF);
                }
                
                if(bankAccountDAO.update(bankAccount) > 0){
                    showAlert(AlertType.INFORMATION, "Successo", "Il conto corrente è stato modificato con successo.", "Il conto corrente è stato modificato con successo.");
                }

            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                System.err.println("Errore: " + e.getMessage());
            }  catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il saldo inserito non è valido.");
                System.err.println("Errore: " + e.getMessage());
            }
        }
    }

}
