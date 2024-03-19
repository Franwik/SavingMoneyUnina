package smu.Boundary.Home;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import smu.Boundary.BaseDialog;
import smu.Control.HomeControl;
import smu.DTO.Familiar;

import java.util.*;
import java.io.IOException;
import java.time.*;

public class EditFamiliarDialogController extends BaseDialog {

    @FXML
    private ComboBox<String> familiarChooser;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private void updateFamiliar() throws IOException {

        //Fields from page
        String name = nameField.getText();
        String surname = surnameField.getText();
        String cf = familiarChooser.getSelectionModel().getSelectedItem();
        LocalDate dob = dobField.getValue();

        HomeControl.update(name, surname, cf, dob);

    }

    @FXML
    private void loadFamiliarInfo(){

        Familiar familiar = HomeControl.getFamiliarInfo(familiarChooser.getSelectionModel().getSelectedItem().toString());

        nameField.setText(familiar.getName());
        surnameField.setText(familiar.getSurname());
        dobField.setValue(familiar.getDateOfBirth());

    }

    private void loadFamiliars(){

        List<String> result = new ArrayList<>();

        List<Familiar> familiars = HomeControl.getFamiliars();
        
        for (Familiar familiar : familiars)
            result.add(familiar.getCF());

        familiarChooser.getItems().addAll(result);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {

        loadFamiliars();

    }

}