package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void switchToRegister() throws IOException {
        App.setScene("Register", 716, 539);
    }

    @FXML
    private void login() throws IOException {
        System.out.println("ci siamo");
        App.setScene("HomePage", 1296, 759);
        App.Resizable(true);
        App.Center();
    }

}
