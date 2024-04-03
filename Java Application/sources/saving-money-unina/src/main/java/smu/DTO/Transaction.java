package smu.DTO;

import java.time.*;

public class Transaction {

	//Attributes
    private int ID_Transaction;
    private Float amount;
    private LocalDate date;
    private String category;
    private String walletName;
    private String cardNumber;

	//Constructor
    public Transaction(int ID_Transaction, Float amount, LocalDate date, String category, String walletName, String cardNumber) {
        this.ID_Transaction = ID_Transaction;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.walletName = walletName;
        this.cardNumber = cardNumber;
    }

    //Overloaded constructor without ID
	public Transaction(Float amount, LocalDate date, String category, String walletName, String cardNumber) {
        this.ID_Transaction = 0;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.walletName = walletName;
        this.cardNumber = cardNumber;
    }

	//Getters and Setters
    public int getID_Transaction() {
        return ID_Transaction;
    }

    public void setID_Transaction(int ID_Transaction) {
        this.ID_Transaction = ID_Transaction;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWalletCategory() {
        return category;
    }

    public void setWalletCategory(String category) {
        this.category = category;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "Transaction [ID_Transaction=" + ID_Transaction + ", amount=" + amount + ", date=" + date + ", category=" + category + " walletName=" + walletName + " cardNumber=" + cardNumber + "]";
    }
}
