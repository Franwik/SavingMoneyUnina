package smu.Boundary;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.App;
import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.Card;
import smu.DTO.Familiar;
import java.util.*;
import java.sql.*;
import java.io.IOException;

public class DeleteCardDialogController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<String> cardChoser;

    @SuppressWarnings("unused")
    @FXML
    private void deleteCard() throws IOException {
        //DAO to interact with DB
        CardDAO cardDAO = new CardDAOimp();

        //Fields from page
        String card_number = cardChoser.getSelectionModel().getSelectedItem();

        //Aletrs to show in case of errors
        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("Almeno uno dei campi è vuoto.");

        Alert wanringAlert = new Alert(AlertType.CONFIRMATION);
        wanringAlert.setTitle("Attenzione");
        wanringAlert.setHeaderText("Vuoi procedere?");
        wanringAlert.setContentText("Sei sicuro di voler eliminare la carta " + card_number.toString() + "?");

        //In case one of the field is empty
        if(card_number == null){
            emptyAlert.showAndWait();
        }
        else{

            try {

                Optional<ButtonType> choice = wanringAlert.showAndWait();

                if(choice.get() == ButtonType.OK){
                    cardDAO.delete(card_number);
                    reload();
                    loadCards();
                }


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

    private void reload(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteCardDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 370, 140);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCards(){

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

            cardChoser.getItems().addAll(result);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        loadCards();
    }

}