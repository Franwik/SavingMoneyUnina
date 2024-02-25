package smu.DTO;

import java.time.*;

public class User {

    // Attricutes
    private String email;
    private String username;
    private String password;
    private String address;
    private String name;
    private String surname;
    private String CF;
    private LocalDate dateOfBirth;

    //Contructor
    public User(String email, String username, String password, String address, String name, String surname, String CF, LocalDate dateOfBirth) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.name = name;
        this.surname = surname;
        this.CF = CF;
        this.dateOfBirth = dateOfBirth;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", username=" + username + ", password=" + password + ", address=" + address
                + ", name=" + name + ", surname=" + surname + ", CF=" + CF + ", dateOfBirth=" + dateOfBirth + "]";
    }

}
