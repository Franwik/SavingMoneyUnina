package smu.Boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import smu.Control.TransactionControl;


public class TransactionPageController extends ApplicationPageController {


    @FXML
    private ComboBox<String> cardChooser;

    @FXML
    private void showNewTransactionDialog() throws IOException{
        System.out.println("mostrato il dialog panel");
    }

    @FXML
    private void loadTransactions(){
        System.out.println("Caricate transazioni");
    }

    private void loadCards(){
        List<String> cards = new ArrayList<>();

        cards = TransactionControl.getCards();
        cardChooser.getItems().addAll(cards);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadCards();

    }

}
