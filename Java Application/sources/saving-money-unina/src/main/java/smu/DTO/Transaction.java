package smu.DTO;

import java.util.*;

public class Transaction {

	//Attributes
    private int ID_Transaction;
    private float amount;
    private Date date;
    private String category;

	//Constructor
    public Transaction(int ID_Transaction, float amount, Date date, String category) {
        this.ID_Transaction = ID_Transaction;
        this.amount = amount;
        this.date = date;
        this.category = category;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction [ID_Transaction=" + ID_Transaction + ", amount=" + amount + ", date=" + date + ", category=" + category + "]";
    }
}
