package smu.DTO;

import java.time.LocalDate;

public class Familiar {

    //Attributes
    private String name;
    private String surname;
    private String CF;
    private LocalDate dateOfBirth;
    private String familiarEmail;

    //Contructor
    public Familiar(String name, String surname, String cF, LocalDate dateOfBirth, String familiarEmail) {
        this.name = name;
        this.surname = surname;
        CF = cF;
        this.dateOfBirth = dateOfBirth;
        this.familiarEmail = familiarEmail;
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

    public String getFamiliarEmail() {
        return familiarEmail;
    }

    public void setFamiliarEmail(String familiarEmail) {
        this.familiarEmail = familiarEmail;
    }



    

}
