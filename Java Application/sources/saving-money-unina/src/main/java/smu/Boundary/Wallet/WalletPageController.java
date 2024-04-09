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

        transactionList.getItems().clear();

        loadWalletName();

        List<Transaction> transactions = new ArrayList<>();
        String choosenWallet = walletChooser.getSelectionModel().getSelectedItem();
        transactions = WalletControl.getTransaction(choosenWallet);
        transactionList.getItems().addAll(transactions);

    }

    @FXML
    private void loadTransactionsWallet(){
        List<Transaction> transactions = new ArrayList<>();
        String choosenWalletName = walletNameChooser.getSelectionModel().getSelectedItem();

        if(choosenWalletName != null){

            transactionList.getItems().clear();
            
            if(choosenWalletName.equals("---")){
                transactions = WalletControl.getTransaction(walletChooser.getSelectionModel().getSelectedItem());
                transactionList.getItems().addAll(transactions);
            }
            else{
                transactions = WalletControl.getTransactionWallet(choosenWalletName, walletChooser.getSelectionModel().getSelectedItem());
                transactionList.getItems().addAll(transactions);
            }
        }
    }

    private void loadWallets(){

        walletChooser.getItems().clear();
        List<String> uniqueWalletCategories = new ArrayList<>();
    
        List<String> wallets = TransactionControl.getWalletCategory();
    
        for (String category : wallets) {
            if (!uniqueWalletCategories.contains(category)) {
                uniqueWalletCategories.add(category);
            }
        }
    
        walletChooser.getItems().add("---");
        walletChooser.getItems().addAll(uniqueWalletCategories);
    }

    @FXML
    private void loadWalletName(){

        walletNameChooser.getItems().clear();
        List<String> walletsName = new ArrayList<>();

        walletsName = TransactionControl.getWalletNameByCategory(walletChooser.getSelectionModel().getSelectedItem());

        walletNameChooser.getItems().add("---");
        walletNameChooser.getItems().addAll(walletsName);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ID_Transaction.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("ID_Transaction"));
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("amount"));
        date.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("date"));
        walletName.setCellValueFactory(new PropertyValueFactory<Transaction, String>("walletName")); 

        loadWallets();

        loadWalletName();
    }

}
