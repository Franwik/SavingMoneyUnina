package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void logout() throws IOException {
        System.out.println("Arrivederci");
        App.setScene("Login");
    }

    @FXML
    private void switchToHome() throws IOException {
        App.setScene("Home");
    }

    @FXML
    private void switchToBankAccount() throws IOException {
        App.setScene("BankAccount");
    }

    @FXML
    private void switchToCard() throws IOException {
        App.setScene("Card");
    }

    @FXML
    private void switchToTransaction() throws IOException {
        App.setScene("Transaction");
    }

    @FXML
    private void switchToWallet() throws IOException {
        App.setScene("Wallet");
    }
}
