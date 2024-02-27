package smu.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class TransactionController extends ApplicationController implements Initializable{

    @FXML
    private void showNewTransactionDialog() throws IOException{
        System.out.println("mostrato il dialog panel");
    }

    @FXML
    private void loadTransactions(){
        System.out.println("Caricate transazioni");
    }

    private void loadCards(){
        System.out.println("Carte caricate");
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadCards();

    }

}
