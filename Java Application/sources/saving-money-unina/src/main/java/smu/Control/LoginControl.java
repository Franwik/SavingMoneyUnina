package smu.Control;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;
import smu.App;
import smu.LoggedUser;
import smu.DAO.UserDAO;
import smu.DAOImplementation.UserDAOimp;
import smu.DTO.User;

public class LoginControl extends BaseControl{

    public static void login(String email, String password){

        if(email.isEmpty() || password.isEmpty()){
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "I campi Email e/o Password sono vuoti.");
        }
        else{

            try {

                UserDAO userDAO = new UserDAOimp();
                User user = userDAO.checkLogin(email, password);

                //If a user is found...
                if(user != null){
                    //sets logged user instance..
                    LoggedUser loggedUser = LoggedUser.getInstance(user);
                    System.out.println(loggedUser);
                    //switches to home page of application
                    App.setRoot("Home");
                }
                else{
                    showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore.", "Email e/o password errati.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
