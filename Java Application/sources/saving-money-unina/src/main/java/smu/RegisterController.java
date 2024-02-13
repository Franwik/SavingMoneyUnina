package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void switchToLogin() throws IOException {
        App.setScene("Login");
    }

    @FXML
    private void register() throws IOException {
        System.out.println("bravo");
        App.setScene("Login");
    }
}