package smu.DTO;

import java.time.LocalDate;

public class ReportCard extends Card{

    //Attributes
    float minIN;
    float maxIN;
    float minOUT;
    float maxOUT;

    //Contructor
    public ReportCard(String cardNumber, String iban, String cvv, LocalDate expireDate, String cardType, int ba_number,
            String ownerCF, String ownerEmail, float minIN, float maxIN, float minOUT, float maxOUT) {
        super(cardNumber, iban, cvv, expireDate, cardType, ba_number, ownerCF, ownerEmail);
        this.minIN = minIN;
        this.maxIN = maxIN;
        this.minOUT = minOUT;
        this.maxOUT = maxOUT;
    }

    // Getters and setters

    public float getMinIN() {
        return minIN;
    }


    public void setMinIN(float minIN) {
        this.minIN = minIN;
    }


    public float getMaxIN() {
        return maxIN;
    }


    public void setMaxIN(float maxIN) {
        this.maxIN = maxIN;
    }


    public float getMinOUT() {
        return minOUT;
    }


    public void setMinOUT(float minOUT) {
        this.minOUT = minOUT;
    }


    public float getMaxOUT() {
        return maxOUT;
    }


    public void setMaxOUT(float maxOUT) {
        this.maxOUT = maxOUT;
    }
    

    @Override
    public String toString() {
        return "Card [cardNumber=" + super.getCardNumber() + ", iban=" + super.getIban() + ", cvv=" + super.getCvv() + ", expireDate=" + super.getExpireDate()
                + ", cardType=" + super.getCardType() + ", ba_number=" + super.getBa_number() + ", ownerCF=" + super.getOwnerCF() + ", ownerEmail="
                + super.getOwnerEmail() +  ", minIN=" + minIN + ", maxIN=" + maxIN + ", minOUT=" + minOUT + ", maxOUT=" + maxOUT + "]";
    }

}
