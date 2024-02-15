package smu;

import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

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
        App.setScene("Register");
    }

    @FXML
    private void login() throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();

        Alert emptyAlert = new Alert(AlertType.ERROR);
        emptyAlert.setTitle("Errore");
        emptyAlert.setHeaderText("Si è verificato un errore.");
        emptyAlert.setContentText("I campi Email o Password sono vuoti.");

        Alert wrongLogin = new Alert(AlertType.ERROR);
        wrongLogin.setTitle("Errore");
        wrongLogin.setHeaderText("Si è verificato un errore.");
        wrongLogin.setContentText("Email o password errati.");

        if(email.isEmpty() || password.isEmpty()){
            emptyAlert.showAndWait();
        }
        else{
            DBConnection connectNow = new DBConnection();
            Connection connectDB = connectNow.GetConnection();
            String query = "SELECT * FROM smu.user WHERE email = '" + email + "' AND password = '" + password + "'";

            try {
                
                java.sql.Statement statement = connectDB.createStatement();
                ResultSet result = statement.executeQuery(query);

                if(result.next()){

                    App.setScene("Home");
    
                }
                else{
                    wrongLogin.showAndWait();
                }

                connectDB.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
