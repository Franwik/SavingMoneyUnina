package smu.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.time.*;
import java.sql.*;
import smu.App;
import smu.DAO.UserDAO;
import smu.DAOImplementation.UserDAOimp;
import smu.DTO.User;

public class RegisterController {

    @FXML
    TextField nameField;

    @FXML
    TextField surnameField;

    @FXML
    TextField addressField;

    @FXML
    DatePicker dobField;

    @FXML
    TextField cfField;

    @FXML
    TextField usernameField;

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void register() throws IOException {

        //DAO to interact with DB
        UserDAO userDAO = new UserDAOimp();

        //all attributes from register page
        String name = nameField.getText();
        String surname = surnameField.getText();
        String address = addressField.getText();
        LocalDate DOB = dobField.getValue();
        String CF = cfField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        

        //Alerts in case of errors
        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("Uno dei campi è vuoto.");

        Alert successAlert = new Alert(AlertType.CONFIRMATION);
        successAlert.setTitle("Successo");
        successAlert.setContentText("Registrazione avvenuta con successo.");

        if(name.isEmpty() || surname.isEmpty() || address.isEmpty() || DOB == null || CF.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            emptyAlert.showAndWait();
        }else{
            try {

                User user = new User(email, username, password, address, username, surname, CF, DOB);

                if(userDAO.insert(user) > 0){
                    successAlert.showAndWait();
                    App.setRoot("Home");
                }
                else{
                    System.out.println("SYSTEM ERROR");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}