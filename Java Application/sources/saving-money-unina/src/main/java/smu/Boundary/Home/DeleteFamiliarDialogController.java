package smu.Boundary.Home;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.App;
import smu.Boundary.BaseDialog;
import smu.Control.CardControl;
import java.util.*;
import java.io.IOException;

public class DeleteFamiliarDialogController extends BaseDialog {

    @FXML
    private ComboBox<String> cardChoser;

    @FXML
    private void deleteCard() throws IOException {

        //Fields from page
        String cardNumber = cardChoser.getSelectionModel().getSelectedItem();

        CardControl.delete(cardNumber);

        reload();
        loadCards();
    }

    private void reload(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteCardDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 370, 140);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCards(){

        List<String> result = new ArrayList<>();
        
        result = CardControl.getAllCards();

        cardChoser.getItems().addAll(result);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        loadCards();
    }

}