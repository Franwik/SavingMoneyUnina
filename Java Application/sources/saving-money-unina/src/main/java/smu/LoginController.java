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
        App.setResolution(1330, 770);
        App.setScene("Home");
        //App.Resizable(true);
        App.Center();
    }

}
