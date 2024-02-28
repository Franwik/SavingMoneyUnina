package smu.DTO;

import java.time.LocalDate;

public class Card {

    //Attributes
    private String cardNumber;
    private String iban;
    private String cvv;
    private LocalDate expireDate;
    private String cardType;
    private int ba_number;
    private String ownerCF;
    private String ownerEmail;

    //Contructor
    public Card(String cardNumber, String iban, String cvv, LocalDate expireDate, String cardType, int ba_number, String ownerCF, String ownerEmail) {
        this.cardNumber = cardNumber;
        this.iban = iban;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.cardType = cardType;
        this.ba_number = ba_number;
        this.ownerCF = ownerCF;
        this.ownerEmail = ownerEmail;
    }

    // Getters and setters

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getBa_number() {
        return ba_number;
    }

    public void setBa_number(int ba_number) {
        this.ba_number = ba_number;
    }

    public String getOwnerCF() {
        return ownerCF;
    }

    public void setOwnerCF(String ownerCF) {
        this.ownerCF = ownerCF;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    @Override
    public String toString() {
        return "Card [cardNumber=" + cardNumber + ", iban=" + iban + ", cvv=" + cvv + ", expireDate=" + expireDate
                + ", cardType=" + cardType + ", ba_number=" + ba_number + ", ownerCF=" + ownerCF + ", ownerEmail="
                + ownerEmail + "]";
    }
    
}
