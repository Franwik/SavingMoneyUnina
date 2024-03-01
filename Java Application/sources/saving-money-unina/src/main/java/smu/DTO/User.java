package smu.DTO;

import java.time.*;

public class User extends Person{

    // Attricutes
    private String email;
    private String username;
    private String password;
    private String address;

    //Contructor
    public User(String name, String surname, String cF, LocalDate dateOfBirth, String email, String username, String password, String address) {
        super(name, surname, cF, dateOfBirth);
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    //Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", username=" + username + ", password=" + password + ", address=" + address
                + ", name=" + super.getName() + ", surname=" + super.getSurname() + ", CF=" + super.getCF() + ", dateOfBirth=" + super.getDateOfBirth() + "]";
    }

}
