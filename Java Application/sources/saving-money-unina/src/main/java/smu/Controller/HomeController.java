package smu.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import smu.App;
import smu.LoggedUser;

public class HomeController implements Initializable{

    @FXML
    Label welcomeLabel;

    @FXML
    private void logout() throws IOException {
        LoggedUser.cleanUserSession();
        System.out.println("Arrivederci");
        App.setRoot("Login");
    }

    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("Home");
    }

    @FXML
    private void switchToBankAccount() throws IOException {
        App.setRoot("BankAccount");
    }

    @FXML
    private void switchToCard() throws IOException {
        App.setRoot("Card");
    }

    @FXML
    private void switchToTransaction() throws IOException {
        App.setRoot("Transaction");
    }

    @FXML
    private void switchToWallet() throws IOException {
        App.setRoot("Wallet");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        welcomeLabel.setText("Benvenuto/a,\n" + loggedUser.getName() + " " + loggedUser.getSurname());
    }
}
