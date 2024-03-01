package smu.DTO;

import java.time.*;

public abstract class Person {

    //Attributes
    private String name;
    private String surname;
    private String CF;
    private LocalDate dateOfBirth;

    //Contructor
    public Person(String name, String surname, String cF, LocalDate dateOfBirth) {
        this.name = name;
        this.surname = surname;
        CF = cF;
        this.dateOfBirth = dateOfBirth;
    }

    //Getters and Setters

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

    public void setCF(String cF) {
        CF = cF;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", surname=" + surname + ", CF=" + CF + ", dateOfBirth=" + dateOfBirth + "]";
    }  
    
}
