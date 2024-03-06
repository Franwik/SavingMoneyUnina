package smu.Control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import smu.DTO.Card;
import smu.DTO.Familiar;
import smu.DTO.Person;

public class CardControl extends BaseControl{

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
                e.printStackTrace();
            }
            
            return familiar;
        }
        
    }

    public static String getCardOwnerCF(String cardNumber){

        CardDAO cardDAO = new CardDAOimp();
        UserDAO userDAO = new UserDAOimp();
        Card card = null;
        String CF = null;

        try {
            card = cardDAO.getByNumber(cardNumber);
            if (card.getOwnerCF() != null) {
                CF = card.getOwnerCF();
            }
            else {
                CF = userDAO.getByEmail(card.getOwnerEmail()).getCF();
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            e.printStackTrace();
        }

        return CF;
    }

    public static Card getCardInfo(String cardNumber){

        Card card = null;
        CardDAO cardDAO = new CardDAOimp();

        try {
            card = cardDAO.getByNumber(cardNumber);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            e.printStackTrace();
        }

        return card;
    }

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
            e.printStackTrace();
        }

        return peopleCFList;
    }
    
    public static List<Card> getCards(String chosenPerson){

        List<Card> cards = new ArrayList<>();

        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        try {
            
            if (chosenPerson.equals(loggedUser.getCF())) {
                cards = cardDAO.getByEmail(loggedUser.getEmail());
            }
            else {
                cards = cardDAO.getByCF(chosenPerson);
            }
            
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            e.printStackTrace();
        }

        return cards;
    }

    public static List<String> getAllCards(){

        List<String> result = new ArrayList<>();
        List<Familiar> familiars = new ArrayList<>();
        List<Card> cards = new ArrayList<>();

        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();
        
        try {
            
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            cards.addAll(cardDAO.getByEmail(loggedUser.getEmail()));

            for(Familiar familiar : familiars){
                cards.addAll(cardDAO.getByCF(familiar.getCF()));
            }

            for(Card card : cards){
                result.add(card.getCardNumber());
            }
            
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            e.printStackTrace();
        }

        return result;
    }

    public static List<Integer> getAllBA(){

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
            e.printStackTrace();
        }

        return result;

    }

    public static void delete(String cardNumber){
        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //In case one of the field is empty
        if(cardNumber == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Non è stata selezionata nessuna carta.");
        }
        else{

            try {

                Optional<ButtonType> choice = showAlert(AlertType.CONFIRMATION, "Attenzione", "Vuoi procedere?", "Sei sicuro di voler eliminare la carta " + cardNumber.toString() + "?");

                if(choice.get() == ButtonType.OK){
                    cardDAO.delete(cardNumber);
                }


            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                e.printStackTrace();
            }

        }

    }

    public static void insert (String cardNumber, String iban, String cvv, LocalDate expireDate, String type, Integer ba_number, String ownerCF) {
        
        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //instance of logged user
        LoggedUser loggedUser = LoggedUser.getInstance();

        //Card that needs to be inserted
        Card card = null;

        //checks on input
        if(cardNumber.isEmpty() || iban.isEmpty() || cvv.isEmpty() || expireDate == null || type == null || ba_number == null || ownerCF == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        else if (cardNumber.length() < 16){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il numero di carta inserito è troppo corto.");
        }
        else if (cardNumber.length() > 16){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il numero di carta inserito è troppo lungo.");
        }
        else if (iban.length() < 27) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "L'iban inserito è troppo corto.");
        }
        else if (iban.length() > 27) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "L'iban inserito è troppo lungo.");
        }
        else if (cvv.length() < 3) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il cvv inserito è troppo corto.");
        }
        else if (cvv.length() > 3) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il cvv inserito è troppo lungo.");
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

                showAlert(AlertType.INFORMATION, "Successo", "Nuova carta inserita con successo.", "Nuova carta inserita con successo.");


            } catch (SQLException e) {
                e.printStackTrace();
                String state = e.getSQLState();
                System.out.println("codice: " + state);
                if (state.equals("23505")){
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La carta che si sta tentando di inserire esiste già.");
                }
                else{
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                }
            }

        }


    }

    public static void update (String cardNumber, String iban, String cvv, LocalDate expireDate, String type, Integer ba_number, String ownerCF) {
        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //instance of logged user
        LoggedUser loggedUser = LoggedUser.getInstance();

        //Card that needs to be inserted
        Card card = null;

        //checks on input
        if(cardNumber.isEmpty() || iban.isEmpty() || cvv.isEmpty() || expireDate == null || type == null || ba_number == null || ownerCF == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        else if (cardNumber.length() < 16){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il numero di carta inserito è troppo corto.");
        }
        else if (cardNumber.length() > 16){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il numero di carta inserito è troppo lungo.");
        }
        else if (iban.length() < 27) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "L'iban inserito è troppo corto.");
        }
        else if (iban.length() > 27) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "L'iban inserito è troppo lungo.");
        }
        else if (cvv.length() < 3) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il cvv inserito è troppo corto.");
        }
        else if (cvv.length() > 3) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il cvv inserito è troppo lungo.");
        }
        else{

            try {

                if(ownerCF.equals(loggedUser.getCF())){
                    card = new Card(cardNumber, iban, cvv, expireDate, type, ba_number, null, loggedUser.getEmail());
                }
                else{
                    card = new Card(cardNumber, iban, cvv, expireDate, type, ba_number, ownerCF, null);
                }
                
                if(cardDAO.update(card) > 0){
                    showAlert(AlertType.INFORMATION, "Successo", "La carta è stata modificata con successo.", "La carta è stata modificata con successo.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                String state = e.getSQLState();
                System.out.println("codice: " + state);
                if (state.equals("23505")){
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La carta che si sta tentando di inserire esiste già.");
                }
                else{
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                }
            }

        }
    }

}
