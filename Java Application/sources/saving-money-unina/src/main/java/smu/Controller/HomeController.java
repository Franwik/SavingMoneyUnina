package smu.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import smu.App;

public class HomeController {

    @FXML
    private void logout() throws IOException {
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
}
