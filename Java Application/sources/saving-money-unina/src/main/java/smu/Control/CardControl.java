package smu.Control;

import java.sql.SQLException;
import java.util.*;
import javafx.scene.control.Alert.AlertType;
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

}
