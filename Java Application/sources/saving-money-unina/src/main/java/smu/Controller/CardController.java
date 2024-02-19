package smu.Controller;

import java.time.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import smu.App;
import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;
import java.io.IOException;

public class CardController extends ApplicationController implements Initializable{

    @FXML
    private ComboBox<String> peopleChoser;

    @FXML
    private Text nameDisplay;

    @FXML
    private TableView<Card> cardList;

    @FXML
    private TableColumn<Card, String> IBAN;
    
    @FXML
    private TableColumn<Card, String> CVV;

    @FXML
    private TableColumn<Card, LocalDate> expireDate;

    @FXML
    private TableColumn<Card, String> type;

    @FXML
    private TableColumn<Card, Integer> BA_ass;

    private static Scene NewCardscene;
    private static Stage NewCardStage;

    @FXML
    private void showNewCardDialog() throws IOException {
        NewCardscene = new Scene(App.loadFXML("NewCardDialog").load(), 370, 365);
        NewCardStage.setScene(NewCardscene);
        NewCardStage.setTitle("Creazione Nuova Carta");
        NewCardStage.setResizable(false);
        NewCardStage.show();
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

            peopleChoser.getItems().add("---");
            peopleChoser.getItems().addAll(people);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadCards(){

        List<Card> cards = new ArrayList<>();
        String chosenPerson = peopleChoser.getSelectionModel().getSelectedItem().toString();

        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        //Current user
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        
        try {

            cardList.getItems().clear();

            if(chosenPerson.equals("---")){
                nameDisplay.setText("");
            }
            else {
                if(chosenPerson.equals(loggedUser.getCF())) {

                    nameDisplay.setText(loggedUser.getName() + " " + loggedUser.getSurname());
                    cards = cardDAO.getByEmail(loggedUser.getEmail());
                }
                else {

                    Familiar familiar = familiarDAO.getByCF(chosenPerson);

                    nameDisplay.setText(familiar.getName() + " " + familiar.getSurname());

                    cards = cardDAO.getByCF(chosenPerson);

                }

                cardList.getItems().addAll(cards);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    

    public void initialize(URL location, ResourceBundle resources) {

        //Loads People in ComboBox
        loadPeople();

        //Initialize Stage
        NewCardStage = new Stage();

        //Initalize table columns
        IBAN.setCellValueFactory(new PropertyValueFactory<Card, String>("iban"));
        CVV.setCellValueFactory(new PropertyValueFactory<Card, String>("cvv"));
        expireDate.setCellValueFactory(new PropertyValueFactory<Card, LocalDate>("expireDate"));
        type.setCellValueFactory(new PropertyValueFactory<Card, String>("cardType"));
        BA_ass.setCellValueFactory(new PropertyValueFactory<Card, Integer>("ba_number"));

    }

}
