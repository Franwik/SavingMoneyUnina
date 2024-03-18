package smu.DTO;

import java.time.LocalDate;

public class ReportCard extends Card{

    Integer minIN;
    Integer maxIN;
    Integer minOUT;
    Integer maxOUT;

    public ReportCard(String cardNumber, String iban, String cvv, LocalDate expireDate, String cardType, int ba_number, String ownerCF, String ownerEmail, Integer minIN, Integer maxIN, Integer minOUT, Integer maxOUT) {
        super(cardNumber, iban, cvv, expireDate, cardType, ba_number, ownerCF, ownerEmail);
        this.minIN = minIN;
        this.maxIN = maxIN;
        this.minOUT = minOUT;
        this.maxOUT = maxOUT;
    }

    public Integer getMinIN() {
        return minIN;
    }

    public void setMinIN(Integer minIN) {
        this.minIN = minIN;
    }

    public Integer getMaxIN() {
        return maxIN;
    }

    public void setMaxIN(Integer maxIN) {
        this.maxIN = maxIN;
    }

    public Integer getMinOUT() {
        return minOUT;
    }

    public void setMinOUT(Integer minOUT) {
        this.minOUT = minOUT;
    }

    public Integer getMaxOUT() {
        return maxOUT;
    }

    public void setMaxOUT(Integer maxOUT) {
        this.maxOUT = maxOUT;
    }

    @Override
    public String toString() {
        return "Card [cardNumber=" + super.getCardNumber() + ", iban=" + super.getIban() + ", cvv=" + super.getCvv() + ", expireDate=" + super.getExpireDate()
                + ", cardType=" + super.getCardType() + ", ba_number=" + super.getBa_number() + ", ownerCF=" + super.getOwnerCF() + ", ownerEmail="
                + super.getOwnerEmail() +  ", minIN=" + minIN + ", maxIN=" + maxIN + ", minOUT=" + minOUT + ", maxOUT=" + maxOUT + "]";
    }
    
}
