package smu.Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import smu.LoggedUser;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;

public class CardController extends HomeController implements Initializable{

    @FXML
    private ComboBox<String> peopleChoser;

    @FXML
    private ListView<Card> cardList;
    
    private List<String> people = new ArrayList<>();
    private List<Familiar> familiars = new ArrayList<>();
    

    public void initialize(URL location, ResourceBundle resources) {

        //DAO to interact with DB
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        
        try {
            
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());

            people.add(loggedUser.getName() + " " + loggedUser.getSurname());

            for(Familiar familiar : familiars){
                people.add(familiar.getName() + " " + familiar.getSurname());
            }

            peopleChoser.getItems().addAll(people);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
