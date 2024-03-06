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

import java.util.*;
import java.io.IOException;
import java.time.*;

public class NewCardDialogController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private TextField cardNumberField;

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
    private void createCard() throws IOException {

        //Fields from page
        String cardNumber = cardNumberField.getText();
        String iban = ibanField.getText();
        String cvv = cvvField.getText();
        LocalDate expireDate = expDField.getValue();
        String type = typeChoser.getSelectionModel().getSelectedItem();
        Integer ba_number = baChoser.getSelectionModel().getSelectedItem();
        String ownerCF = ownerChoser.getSelectionModel().getSelectedItem();

        CardControl.insert(cardNumber, iban, cvv, expireDate, type, ba_number, ownerCF);

    }

    @FXML
    private void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void loadPeople(){

        List<String> people = new ArrayList<>();

        people = CardControl.loadPeople();

        ownerChoser.getItems().addAll(people);

    }

    private void loadBA(){

        List<Integer> bankAccounts = new ArrayList<>();

        bankAccounts = CardControl.loadAllBA();

        baChoser.getItems().addAll(bankAccounts);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        typeChoser.getItems().addAll(cardTypes);

        loadPeople();

        loadBA();
    }

}