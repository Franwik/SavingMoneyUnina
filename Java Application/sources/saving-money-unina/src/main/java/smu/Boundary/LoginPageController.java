package smu.Boundary;

import java.io.IOException;
import smu.App;
import smu.Control.LoginControl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("Register");
    }

    @FXML
    private void login() throws IOException {

        //email and password from Login page
        String email = emailField.getText();
        String password = passwordField.getText();

        LoginControl.login(email, password);

    }
}
