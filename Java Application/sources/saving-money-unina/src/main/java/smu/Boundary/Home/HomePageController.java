package smu.Boundary.Home;

import java.text.DateFormatSymbols;
import java.time.*;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.LoggedUser;
import smu.Boundary.ApplicationPageController;
import smu.Control.HomeControl;
import smu.DTO.Familiar;
import smu.DTO.ReportCard;

public class HomePageController extends ApplicationPageController {

    @FXML
    Label welcomeLabel;

    @FXML
    private ComboBox<String> monthChooser;

    @FXML
    private TextField yearField;

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

    @FXML
    private TableView<Familiar> familiarsList;

    @FXML
    private TableColumn<Familiar, String> name;
    
    @FXML
    private TableColumn<Familiar, String> surname;

    @FXML
    private TableColumn<Familiar, String> CF;

    @FXML
    private TableColumn<Familiar, LocalDate> dob;

    private void setWelcomeLabel() {
        LoggedUser loggedUser = LoggedUser.getInstance();
        welcomeLabel.setText("Benvenuto/a, " + loggedUser.getName() + " " + loggedUser.getSurname());
    }

    @FXML
    private void loadReport(){

        String chosenMonth = monthChooser.getSelectionModel().getSelectedItem();
        String year = yearField.getText();

        List<ReportCard> reportCards = HomeControl.getReports(chosenMonth, year);

        reportList.getItems().clear();
        reportList.getItems().addAll(reportCards);

    }

    private static String capitalizeFirstLetter(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    private void loadFamiliars() {

        List<Familiar> familiars = HomeControl.getFamiliars();

        familiarsList.getItems().clear();

        familiarsList.getItems().addAll(familiars);

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

        //Initalize report table columns
        cardNumber.setCellValueFactory(new PropertyValueFactory<ReportCard, String>("cardNumber"));
        ownerCF.setCellValueFactory(new PropertyValueFactory<ReportCard, String>("ownerCF"));
        minIN.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("minIN"));
        maxIN.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("maxIN"));
        minOUT.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("minOUT"));
        maxOUT.setCellValueFactory(new PropertyValueFactory<ReportCard, Float>("maxOUT"));

        //Initialize familiar table columns
        name.setCellValueFactory(new PropertyValueFactory<Familiar,String>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<Familiar,String>("surname"));
        CF.setCellValueFactory(new PropertyValueFactory<Familiar,String>("CF"));
        dob.setCellValueFactory(new PropertyValueFactory<Familiar,LocalDate>("dateOfBirth"));

        //Load current familiars
        loadFamiliars();

    }

    
}
