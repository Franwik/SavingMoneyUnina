package smu.Controller;

import java.io.IOException;
import java.sql.*;

import smu.App;
import smu.LoggedUser;
import smu.DAO.UserDAO;
import smu.DAOImplementation.UserDAOimp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("Register");
    }

    @FXML
    private void login() throws IOException {

        //DAO to interact with DB
        UserDAO userDAO = new UserDAOimp();

        //Singleton of logged user
        LoggedUser loggedUser = null;

        //email and password from Login page
        String email = emailField.getText();
        String password = passwordField.getText();

        //Aletrs to show in case of errors
        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("I campi Email e/o Password sono vuoti.");

        Alert wrongLogin = new Alert(AlertType.ERROR);
        wrongLogin.setTitle("Errore");
        wrongLogin.setHeaderText("Si è verificato un errore.");
        wrongLogin.setContentText("Email e/o password errati.");

        //In case one of the field is empty
        if(email.isEmpty() || password.isEmpty()){
            emptyAlert.showAndWait();
        }
        else{

            try {

                loggedUser = LoggedUser.getInstance(userDAO.checkLogin(email, password));

                if(loggedUser != null){
                    App.setRoot("Home");
                }
                else{
                    wrongLogin.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
