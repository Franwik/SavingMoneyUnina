package smu.Boundary.Card;

import java.time.*;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import smu.Boundary.ApplicationPageController;
import smu.Control.CardControl;
import smu.DTO.Card;
import smu.DTO.Person;

import java.io.IOException;

public class CardPageController extends ApplicationPageController {

    @FXML
    private ComboBox<String> peopleChoser;

    @FXML
    private Text nameDisplay;

    @FXML
    private TableView<Card> cardList;

    @FXML
    private TableColumn<Card, String> cardNumber;

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

    @FXML
    private void showNewCardDialog() throws IOException {
        showDialog("NewCardDialog", 370, 365, "Creazione Nuova Carta", "Card");
    }

    @FXML
    private void showDeleteCardDialog() throws IOException {
        showDialog("DeleteCardDialog", 370, 140, "Eliminazione Carta", "Card");
    }

    @FXML
    private void showEditCardDialog() throws IOException {
        showDialog("EditCardDialog", 370, 365, "Modifica Carta", "Card");
    }

    private void loadPeople(){

        List<String> people = CardControl.getPeople();

        peopleChoser.getItems().add("---");
        peopleChoser.getItems().addAll(people);

    }

    @FXML
    private void loadCards(){

        List<Card> cards = new ArrayList<>();
        String chosenPerson = peopleChoser.getSelectionModel().getSelectedItem().toString();

        cardList.getItems().clear();

        if(chosenPerson.equals("---")){
            nameDisplay.setText("");
        }
        else {
            cards = CardControl.getCards(chosenPerson);
            cardList.getItems().addAll(cards);
            Person person = CardControl.getPersonInfo(chosenPerson);
            nameDisplay.setText(person.getName() + " " + person.getSurname());
        }


    }
    

    public void initialize(URL location, ResourceBundle resources) {

        //Loads People in ComboBox
        loadPeople();

        //Initalize table columns
        cardNumber.setCellValueFactory(new PropertyValueFactory<Card, String>("cardNumber"));
        IBAN.setCellValueFactory(new PropertyValueFactory<Card, String>("iban"));
        CVV.setCellValueFactory(new PropertyValueFactory<Card, String>("cvv"));
        expireDate.setCellValueFactory(new PropertyValueFactory<Card, LocalDate>("expireDate"));
        type.setCellValueFactory(new PropertyValueFactory<Card, String>("cardType"));
        BA_ass.setCellValueFactory(new PropertyValueFactory<Card, Integer>("ba_number"));

    }

}
