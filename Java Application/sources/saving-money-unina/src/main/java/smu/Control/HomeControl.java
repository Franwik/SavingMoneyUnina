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

}
