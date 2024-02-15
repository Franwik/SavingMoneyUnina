package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void switchToRegister() throws IOException {
        App.setScene("Register");
    }

    @FXML
    private void login() throws IOException {
        System.out.println("ci siamo");
        App.setScene("Home");
    }

}
