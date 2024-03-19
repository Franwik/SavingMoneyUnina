package smu.Boundary.Home;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.App;
import smu.Boundary.BaseDialog;
import smu.Control.HomeControl;
import smu.DTO.Familiar;

import java.util.*;
import java.io.IOException;

public class DeleteFamiliarDialogController extends BaseDialog {

    @FXML
    private ComboBox<String> familiarChooser;

    @FXML
    private void deleteFamiliar() throws IOException {

        //Fields from page
        String cf = familiarChooser.getSelectionModel().getSelectedItem();

        HomeControl.delete(cf);

        reload();
        loadFamiliars();
    }

    private void reload(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("DeleteFamiliarDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 370, 140);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFamiliars(){

        List<String> result = new ArrayList<>();

        List<Familiar> familiars = HomeControl.getFamiliars();
        
        for (Familiar familiar : familiars)
            result.add(familiar.getCF());

        familiarChooser.getItems().addAll(result);

    }


    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        loadFamiliars();
    }

}