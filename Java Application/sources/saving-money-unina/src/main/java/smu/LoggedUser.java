package smu;

import java.time.LocalDate;

import smu.DTO.User;

public class LoggedUser{

    //Instance
    private static LoggedUser instance;

    // Attributes
    private String email;
    private String username;
    private String password;
    private String address;
    private String name;
    private String surname;
    private String CF;
    private LocalDate dateOfBirth;

    //Constructor
    private LoggedUser(String email, String username, String password, String address, String name, String surname, String CF, LocalDate dateOfBirth) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.name = name;
        this.surname = surname;
        this.CF = CF;
        this.dateOfBirth = dateOfBirth;
    }

    public static LoggedUser getInstance(User user) {
        if(instance == null)
            instance = new LoggedUser(user.getEmail(), user.getUsername(), user.getPassword(), user.getAddress(), user.getName(), user.getSurname(), user.getCF(), user.getDateOfBirth());

        return instance;
    }

    public static LoggedUser getInstance() {
        return instance;
    }

    public static void cleanUserSession() {
            instance = null;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCF() {
        return CF;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "LoggedUser [email=" + email + ", username=" + username + ", password=" + password + ", address="
                + address + ", name=" + name + ", surname=" + surname + ", CF=" + CF + ", dateOfBirth=" + dateOfBirth
                + "]";
    }

}
