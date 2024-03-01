package smu;

import java.time.LocalDate;

import smu.DTO.User;

public class LoggedUser extends User{

    //Instance
    private static LoggedUser instance;

    //Constructor
    private LoggedUser(String name, String surname, String CF, LocalDate dateOfBirth, String email, String username, String password, String address) {
        super(name, surname, CF, dateOfBirth, email, username, password, address);
    }

    public static LoggedUser getInstance(User user) {
        if(instance == null)
            instance = new LoggedUser(user.getName(), user.getSurname(), user.getCF(), user.getDateOfBirth(), user.getEmail(), user.getUsername(), user.getPassword(), user.getAddress());
        return instance;
    }

    public static LoggedUser getInstance() {
        return instance;
    }

    public static void cleanUserSession() {
            instance = null;
    }

}
