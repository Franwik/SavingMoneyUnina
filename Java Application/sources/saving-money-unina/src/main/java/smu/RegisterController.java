package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void register() throws IOException {
        System.out.println("bravo");
        App.setRoot("Login");
    }
}