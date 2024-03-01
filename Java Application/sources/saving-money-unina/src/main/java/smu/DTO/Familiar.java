package smu.DTO;

import java.time.LocalDate;

public class Familiar extends Person{

    //Attributes
    private String familiarEmail;

    //Contructor
    public Familiar(String name, String surname, String cF, LocalDate dateOfBirth, String familiarEmail) {
        super(name, surname, cF, dateOfBirth);
        this.familiarEmail = familiarEmail;
    }

    //Getters and Setters

    public String getFamiliarEmail() {
        return familiarEmail;
    }

    public void setFamiliarEmail(String familiarEmail) {
        this.familiarEmail = familiarEmail;
    }

    @Override
    public String toString() {
        return "Familiar [name=" + super.getName() + ", surname=" + super.getSurname() + ", CF=" + super.getCF() +
            ", dateOfBirth=" + super.getDateOfBirth() + "familiarEmail=" + familiarEmail + "]";
    }  

}
