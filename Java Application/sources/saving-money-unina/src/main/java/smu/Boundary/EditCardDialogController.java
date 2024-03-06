package smu.Boundary;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.Control.CardControl;
import smu.DTO.Card;
import java.util.*;
import java.io.IOException;
import java.time.*;

public class EditCardDialogController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<String> cardChoser;

    @FXML
    private TextField ibanField;

    @FXML
    private TextField cvvField;

    @FXML
    private DatePicker expDField;

    @FXML
    private ComboBox<String> typeChoser;

    @FXML
    private ComboBox<Integer> baChoser;

    @FXML
    private ComboBox<String> ownerChoser;

    private String[] cardTypes = {"Prepagata", "Debito", "Credito"};

    @FXML
    private void updateCard() throws IOException {

        //Fields from page
        String cardNumber = cardChoser.getSelectionModel().getSelectedItem();
        String iban = ibanField.getText();
        String cvv = cvvField.getText();
        LocalDate expireDate = expDField.getValue();
        String type = typeChoser.getSelectionModel().getSelectedItem();
        Integer ba_number = baChoser.getSelectionModel().getSelectedItem();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem();

        CardControl.update(cardNumber, iban, cvv, expireDate, type, ba_number, ownerCF);

    }

    @FXML
    private void loadCardInfo(){

        Card card = CardControl.getCardInfo(cardChoser.getSelectionModel().getSelectedItem().toString());

        ibanField.setText(card.getIban());
        cvvField.setText(card.getCvv());
        expDField.setValue(card.getExpireDate());
        typeChoser.setValue(card.getCardType());
        baChoser.setValue(card.getBa_number());
        ownerChoser.setValue(CardControl.getCardOwnerCF(card.getCardNumber()));
        System.out.println(CardControl.getCardOwnerCF(card.getCardNumber()));
    }

    @FXML
    private void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void loadPeople(){

        List<String> people = new ArrayList<>();

        people = CardControl.getPeople();

        ownerChoser.getItems().addAll(people);

    }

    private void loadBA(){

        List<Integer> bankAccounts = new ArrayList<>();

        bankAccounts = CardControl.getAllBA();

        baChoser.getItems().addAll(bankAccounts);

    }

    private void loadCards(){

        List<String> cards = new ArrayList<>();

        cards = CardControl.getAllCards();

        cardChoser.getItems().addAll(cards);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        typeChoser.getItems().addAll(cardTypes);

        loadCards();

        loadPeople();

        loadBA();
    }

}