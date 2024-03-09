package smu.Boundary;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class BaseDialog implements Initializable{

    @FXML
    protected Button closeButton;

    @FXML
    protected void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
