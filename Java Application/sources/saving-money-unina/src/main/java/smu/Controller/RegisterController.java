package smu.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import smu.App;

public class RegisterController {

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void register() throws IOException {
        System.out.println("bravo");
        App.setRoot("Login");
    }
}