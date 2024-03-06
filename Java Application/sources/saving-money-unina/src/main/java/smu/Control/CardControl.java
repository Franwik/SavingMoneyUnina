package smu.Control;

import java.sql.SQLException;
import java.util.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;

public class CardControl extends BaseControl{

    public static List<String> loadPeople(){

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

    public static String getPersonInfo(String CF){

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance();

        if (CF.equals(loggedUser.getCF())) {
            return loggedUser.getName() + " " + loggedUser.getSurname();
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

            return familiar.getName() + " " + familiar.getSurname();
        }

    }

    public static List<Card> loadCards(String chosenPerson){

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

    public static List<String> loadAllCards(){

        List<String> result = new ArrayList<>();
        List<Familiar> familiars = new ArrayList<>();
        List<Card> cards = new ArrayList<>();

        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        
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

}
