package smu.Boundary;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.time.*;
import smu.App;
import smu.Control.RegisterControl;

public class RegisterPageController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker dobField;

    @FXML
    private TextField cfField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void register() throws IOException {

        //all attributes from register page
        String name = nameField.getText();
        String surname = surnameField.getText();
        String address = addressField.getText();
        LocalDate DOB = dobField.getValue();
        String CF = cfField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        RegisterControl.register(name, surname, address, DOB, CF, username, email, password);

        
    }
}