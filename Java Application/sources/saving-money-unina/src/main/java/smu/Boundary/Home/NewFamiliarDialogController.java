package smu.Boundary.Home;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.CardControl;
import smu.Control.HomeControl;

import java.util.*;
import java.io.IOException;
import java.time.*;

public class NewFamiliarDialogController extends BaseDialog {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField cfField;

    @FXML
    private DatePicker dobField;

    @FXML
    private void createFamiliar() throws IOException {

        //Fields from page
        String name = nameField.getText();
        String surname = surnameField.getText();
        String cf = cfField.getText();
        LocalDate dob = dobField.getValue();

        HomeControl.insert(name, surname, cf, dob);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        
    }

}