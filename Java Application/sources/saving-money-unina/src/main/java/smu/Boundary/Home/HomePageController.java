package smu.Boundary.Home;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import smu.LoggedUser;
import smu.Boundary.ApplicationPageController;

public class HomePageController extends ApplicationPageController {

    @FXML
    Label welcomeLabel;

    private void setWelcomeLabel() {
        LoggedUser loggedUser = LoggedUser.getInstance();
        welcomeLabel.setText("Benvenuto/a, " + loggedUser.getName() + " " + loggedUser.getSurname());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setWelcomeLabel();
    }

    
}
