package smu.Boundary.Home;

import java.text.DateFormatSymbols;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.LoggedUser;
import smu.Boundary.ApplicationPageController;
import smu.DTO.ReportCard;

public class HomePageController extends ApplicationPageController {

    @FXML
    Label welcomeLabel;

    @FXML
    private ComboBox<String> monthChooser;

    @FXML
    private TableView<ReportCard> reportList;

    @FXML
    private TableColumn<ReportCard, String> cardNumber;
    
    @FXML
    private TableColumn<ReportCard, String> ownerCF;

    @FXML
    private TableColumn<ReportCard, Float> minIN;

    @FXML
    private TableColumn<ReportCard, Float> maxIN;

    @FXML
    private TableColumn<ReportCard, Float> minOUT;

    @FXML
    private TableColumn<ReportCard, Float> maxOUT;

    private void setWelcomeLabel() {
        LoggedUser loggedUser = LoggedUser.getInstance();
        welcomeLabel.setText("Benvenuto/a, " + loggedUser.getName() + " " + loggedUser.getSurname());
    }

    private static String capitalizeFirstLetter(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ITALIAN);
        String[] monthsIT = dfs.getMonths();

        List<String> months = new ArrayList<>();

        for (String month : monthsIT) {
            if (month != null && !month.isEmpty()) {
                months.add(capitalizeFirstLetter(month));
            }
        }

        monthChooser.getItems().addAll(months);

        setWelcomeLabel();

        //Initalize table columns
        cardNumber.setCellValueFactory(new PropertyValueFactory<ReportCard, String>("cardNumber"));
        ownerCF.setCellValueFactory(new PropertyValueFactory<ReportCard, String>("ownerCF"));
        minIN.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("minIN"));
        maxIN.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("maxIN"));
        minOUT.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("minOUT"));
        maxOUT.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("maxOUT"));

    }

    
}
