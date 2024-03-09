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


public class TransactionControl extends BaseControl{

	public static List<String> getCards() {
		
		List<String> result = new ArrayList<>();
		List<Card> cards = new ArrayList<>();
		List<Familiar> familiars = new ArrayList<>();
	
		FamiliarDAO familiarDAO = new FamiliarDAOimp();
		CardDAO cardDAO = new CardDAOimp();

		LoggedUser loggedUser = LoggedUser.getInstance();

		
		try {
			cards.addAll(cardDAO.getByEmail(loggedUser.getEmail()));

			familiars.addAll(familiarDAO.getByEmail(loggedUser.getEmail()));

			for(Familiar familiar : familiars){
				cards.addAll(cardDAO.getByCF(familiar.getCF()));
			}
		} catch (SQLException e) {
			showAlert(AlertType.ERROR, "Errore", "Si è verificato un problema inaspettato.", "Problemi con il Database.");
			System.err.println("Errore: " + e.getMessage());
		}

		for(Card card : cards){
			result.add(card.getCardNumber());
		}

		return result;
	}

}
