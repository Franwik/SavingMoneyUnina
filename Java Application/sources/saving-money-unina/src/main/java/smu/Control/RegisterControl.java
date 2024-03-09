package smu.Control;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.*;

import javafx.scene.control.Alert.AlertType;
import smu.App;
import smu.DAO.UserDAO;
import smu.DAOImplementation.UserDAOimp;
import smu.DTO.User;

public class RegisterControl extends BaseControl{

    public static void register(String name, String surname, String address, LocalDate DOB, String CF, String username, String email, String password){

        if(name.isEmpty() || surname.isEmpty() || address.isEmpty() || DOB == null || CF.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Almeno uno dei campi è vuoto.");
        }
        else if (!valEmail(email)) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Email inserita non valida.");
        }
        else{
            try {

                UserDAO userDAO = new UserDAOimp();
                User user = new User(name, surname, CF, DOB, email, username, password, address);

                if(userDAO.insert(user) > 0){
                    showAlert(AlertType.CONFIRMATION, "Successo", "Congratulazioni.", "Registrazione avvenuta con successo.");
                    App.setRoot("Login");
                }

            } catch (SQLException e) {
                String state = e.getSQLState();
                System.out.println(state);

                if(state.equals("23514")){
                    //data di nascita non valida
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "La data di nascita inserita non è valida.");
                }
                else if(state.equals("23505")){
                    //email o username già in uso
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Email e/o Username già in uso.");
                }

            } catch (IOException e) {
                System.err.println("Errore: " + e.getMessage());
            }
        }

    }

    private static boolean valEmail (String input) {

        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();

    }

}
