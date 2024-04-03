package smu.Boundary.Wallet;

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
import smu.Boundary.ApplicationPageController;
import smu.Control.TransactionControl;
import smu.Control.WalletControl;
import smu.DTO.Transaction;

public class WalletPageController extends ApplicationPageController {

    @FXML
    private ComboBox<String> walletChooser;

    @FXML
    private ComboBox<String> walletNameChooser;

    @FXML
    private Text nameDisplay;

    @FXML
    private TableView<Transaction> transactionList;

    @FXML
    private TableColumn<Transaction, Integer> ID_Transaction;

    @FXML
    private TableColumn<Transaction, Float> amount;

    @FXML
    private TableColumn<Transaction, LocalDate> date;

    @FXML
    private TableColumn<Transaction, String> walletName;

    @FXML
    private void showNewWalletDialog() throws IOException{
        showDialog("NewWalletDialog", 370, 340, "Nuovo Portafoglio", "Wallet");
    }

    @FXML
    private void showDeleteWalletDialog() throws IOException{
        showDialog("DeleteWalletDialog", 370, 140, "Elimina Portafoglio", "Wallet");
    }

    @FXML
    private void showEditWalletDialog() throws IOException{
        showDialog("EditWalletDialog", 370, 370, "Modifica Portafoglio", "Wallet");
    }

    @FXML
    private void loadTransactions(){
        
        loadWalletName();
        List<Transaction> transactions = new ArrayList<>();
        String choosenWallet = walletChooser.getSelectionModel().getSelectedItem().toString();

        transactionList.getItems().clear();

        if(choosenWallet.equals("---")){
            nameDisplay.setText("");
        }
        else{
            transactions = WalletControl.getTransaction(choosenWallet);
            transactionList.getItems().addAll(transactions);
        }

        System.out.println("Caricate transazioni");

    }

    @FXML
    private void loadTransactionsWallet(){
        List<Transaction> transactions = new ArrayList<>();
        String choosenWalletName = walletNameChooser.getSelectionModel().getSelectedItem().toString();

        transactionList.getItems().clear();
        if(choosenWalletName.equals("---")){
            nameDisplay.setText("");
        }
        else{
            transactions = WalletControl.getTransactionWallet(choosenWalletName);
            transactionList.getItems().addAll(transactions);
        }

        System.out.println("Caricate transazioni");
    }

    private void loadWallets(){

        List<String> wallets = new ArrayList<>();

        wallets = TransactionControl.getWalletCategory();

        walletChooser.getItems().addAll(wallets);

    }

    private void loadWalletName(){

        List<String> wallets = new ArrayList<>();

        wallets = TransactionControl.getWalletNameByCategory(walletChooser.getSelectionModel().getSelectedItem());

        walletNameChooser.getItems().addAll(wallets);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadWallets();

        loadWalletName();

        ID_Transaction.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("ID_Transaction"));
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("amount"));
        date.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("date"));
        walletName.setCellValueFactory(new PropertyValueFactory<Transaction, String>("walletName")); 
    }

}
