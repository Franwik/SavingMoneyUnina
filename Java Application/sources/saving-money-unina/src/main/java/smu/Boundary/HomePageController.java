package smu.Boundary;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import smu.LoggedUser;

public class HomePageController extends ApplicationPageController {

    @FXML
    Label welcomeLabel;

    private void setWelcomeLabel() {
        LoggedUser loggedUser = LoggedUser.getInstance(null);
        welcomeLabel.setText("Benvenuto/a,\n" + loggedUser.getName() + " " + loggedUser.getSurname());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setWelcomeLabel();
    }

    
}
