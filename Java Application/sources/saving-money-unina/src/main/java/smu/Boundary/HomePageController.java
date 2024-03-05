package smu.Boundary;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import smu.LoggedUser;

public class HomePageController extends ApplicationPageController implements Initializable{

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
