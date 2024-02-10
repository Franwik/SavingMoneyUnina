package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Login");
    }
}