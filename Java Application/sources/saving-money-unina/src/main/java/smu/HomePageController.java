package smu;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomePageController {

    @FXML
    private void switchToLogin() throws IOException {
        App.setScene("Login", 716, 539);
        App.Resizable(false);
    }
}
