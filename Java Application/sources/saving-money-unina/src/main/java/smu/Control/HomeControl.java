package smu.Control;

import java.util.*;
import java.time.*;
import java.sql.*;

import smu.LoggedUser;
import smu.DAO.CardDAO;
import smu.DAO.FamiliarDAO;
import smu.DAOImplementation.CardDAOimp;
import smu.DAOImplementation.FamiliarDAOimp;
import smu.DTO.Familiar;
import smu.DTO.ReportCard;
import javafx.scene.control.Alert.AlertType;

public class HomeControl extends BaseControl{

    public static List<ReportCard> getReports(String chosenMonth, String year) {

        CardDAO cardDAO = new CardDAOimp();

        LoggedUser loggedUser = LoggedUser.getInstance();

        List<ReportCard> reports = new ArrayList<>();

        LocalDate firstDay = null;

        LocalDate lastDay = null;
        
        // Mappa per la traduzione dei nomi dei mesi dall'italiano all'inglese
        Map<String, Month> mappaMesi = new HashMap<>();
        mappaMesi.put("Gennaio", Month.JANUARY);
        mappaMesi.put("Febbraio", Month.FEBRUARY);
        mappaMesi.put("Marzo", Month.MARCH);
        mappaMesi.put("Aprile", Month.APRIL);
        mappaMesi.put("Maggio", Month.MAY);
        mappaMesi.put("Giugno", Month.JUNE);
        mappaMesi.put("Luglio", Month.JULY);
        mappaMesi.put("Agosto", Month.AUGUST);
        mappaMesi.put("Settembre", Month.SEPTEMBER);
        mappaMesi.put("Ottobre", Month.OCTOBER);
        mappaMesi.put("Novembre", Month.NOVEMBER);
        mappaMesi.put("Dicembre", Month.DECEMBER);

        Month month = mappaMesi.get(chosenMonth);

        if (month != null && year != null) {

            try {

                // Ottieni il primo giorno del mese corrispondente
                firstDay = LocalDate.of(Integer.valueOf(year), month, 1);
                //System.out.println("Data corrispondente al primo giorno del mese: " + firstDay);

                lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                //System.out.println("Data corrispondente all'ultimo giorno del mese: " + lastDay);

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Inserire un anno valido.");
                System.err.println("Errore: " + e.getMessage());
            }

        } else {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "I capi mese e/o anno sono vuoti.");
        }

        try {

            reports = cardDAO.getReports(firstDay, lastDay);

            for(ReportCard report : reports) {

                if (report.getOwnerCF() == null) {
                    report.setOwnerCF(loggedUser.getCF());
                }

            }
            
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            System.err.println("Errore: " + e.getMessage());
        }

        return reports;
    }

    public static List<Familiar> getFamiliars() {
        
        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        LoggedUser loggedUser = LoggedUser.getInstance();

        List<Familiar> familiars = new ArrayList<>();

        try {
            familiars = familiarDAO.getByEmail(loggedUser.getEmail());
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
            e.printStackTrace();
        }

        return familiars;
    }

    public static void insert (String name, String surname, String cf, LocalDate dob) {

        FamiliarDAO familiarDAO = new FamiliarDAOimp();

        LoggedUser loggedUser = LoggedUser.getInstance();

        Familiar familiar = null;

        //checks on input
        if(name.isEmpty() || surname.isEmpty() || cf.isEmpty() || dob == null){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        /*else if (cf.length() < 16){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il numero di carta inserito è troppo corto.");
        }*/
        else {

            try {

                familiar = new Familiar(name, surname, cf, dob, loggedUser.getEmail());

                familiarDAO.insert(familiar);
                
                showAlert(AlertType.INFORMATION, "Successo", "Nuovo familiare inserito con successo.", "Nuovo familiare inserito con successo.");
            
            } catch (SQLException e) {
                System.err.println("Errore: " + e.getMessage());
                String state = e.getSQLState();
                System.out.println("codice: " + state);
                if (state.equals("23505")){
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Il familiare che si sta tentando di inserire esiste già.");
                }
                else if (state.equals("23514")) {
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La data di nascita inserita non è valida.");
                }
                else{
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore inaspettato.", "Problemi con il database.");
                }
            }

        }
    } 

}
