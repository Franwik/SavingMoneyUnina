package smu.DTO;

import java.time.*;

public class Transaction {

	//Attributes
    private int ID_Transaction;
    private float amount;
    private LocalDate date;
    private String category;
    private String cardiban;

	//Constructor
    public Transaction(int ID_Transaction, float amount, LocalDate date, String category, String cardiban) {
        this.ID_Transaction = ID_Transaction;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.cardiban = cardiban;

    }

	

	//Getters and Setters
    public int getID_Transaction() {
        return ID_Transaction;
    }

    public void setID_Transaction(int ID_Transaction) {
        this.ID_Transaction = ID_Transaction;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCardiban() {
        return cardiban;
    }

    public void setCardiban(String cardiban) {
        this.cardiban = cardiban;
    }

    @Override
    public String toString() {
        return "Transaction [ID_Transaction=" + ID_Transaction + ", amount=" + amount + ", date=" + date + ", category=" + category + "]";
    }
}
