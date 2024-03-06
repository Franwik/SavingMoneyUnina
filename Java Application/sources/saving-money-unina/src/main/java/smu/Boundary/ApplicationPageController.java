package smu.Boundary;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import smu.App;
import smu.LoggedUser;

public abstract class ApplicationPageController {

    @FXML
    private BorderPane mainPage;

    @FXML
    private void logout() throws IOException {
        LoggedUser.cleanUserSession();
        App.setRoot("Login");
    }

    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("Home");
    }

    @FXML
    private void switchToBankAccount() throws IOException {
        App.setRoot("BankAccount");
    }

    @FXML
    private void switchToCard() throws IOException {
        App.setRoot("Card");
    }

    @FXML
    private void switchToTransaction() throws IOException {
        App.setRoot("Transaction");
    }

    @FXML
    private void switchToWallet() throws IOException {
        App.setRoot("Wallet");
    }

    public void showDialog(String fxml, double width, double height, String title, String root) throws IOException{

        Stage stage = new Stage();
        Scene scene;

        stage.initOwner((Stage) mainPage.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        scene = new Scene(App.loadFXML(fxml).load(), width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.showAndWait();
        App.setRoot(root);
    }

}
