package smu.Boundary;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import smu.Control.TransactionControl;
import smu.DTO.Transaction;


public class TransactionPageController extends ApplicationPageController {


    @FXML
    private ComboBox<String> cardChooser;

    @FXML
    private Text nameDisplay;

    @FXML
    private TableView<Transaction> transactionList;

    @FXML
    private TableColumn<Transaction, String> ID_Transaction;

    @FXML
    private TableColumn<Transaction, String> amount;

    @FXML
    private TableColumn<Transaction, LocalDate> date;

    @FXML
    private TableColumn<Transaction, String> category;

    @FXML
    private TableColumn<Transaction, String> walletName;


    @FXML
    private void showNewTransactionDialog() throws IOException{
        System.out.println("mostrato il dialog panel");
    }

    @FXML
    private void loadTransactions(){

        List<Transaction> transactions = new ArrayList<>();
        String choosenCard = cardChooser.getSelectionModel().getSelectedItem().toString();

        transactionList.getItems().clear();

        if(choosenCard.equals("---")){
            nameDisplay.setText("");
        }
        else{
            transactions = TransactionControl.getTransactions(choosenCard);
            transactionList.getItems().addAll(transactions);

            //TODO: getCardInfo
        }

        System.out.println("Caricate transazioni");
    }

    private void loadCards(){
        List<String> cards = new ArrayList<>();

        cards = TransactionControl.getCards();
        
        cardChooser.getItems().add("---");
        cardChooser.getItems().addAll(cards);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadCards();

        ID_Transaction.setCellValueFactory(new PropertyValueFactory<Transaction, String>("ID_Transaction"));
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount"));
        date.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("date"));
        category.setCellValueFactory(new PropertyValueFactory<Transaction, String>("category"));
        walletName.setCellValueFactory(new PropertyValueFactory<Transaction, String>("walletName"));
    }

}
