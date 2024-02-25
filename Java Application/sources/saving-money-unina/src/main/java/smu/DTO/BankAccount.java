package smu.DTO;


public class BankAccount {

    //Attributes
    private int balance;
    private int accountNumber;
    private String bank;
    private String ownerEmail;
    private String ownerCF;

    //Constructor
    public BankAccount(int balance, int accountNumber, String bank, String ownerEmail, String ownerCF) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.bank = bank;
        this.ownerEmail = ownerEmail;
        this.ownerCF = ownerCF;
    }

    //Getters and Setters

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerCF() {
        return ownerCF;
    }

    public void setOwnerCF(String ownerCF) {
        this.ownerCF = ownerCF;
    }

    @Override
    public String toString() {
        return "BankAccount [balance=" + balance + ", accountNumber=" + accountNumber + ", bank=" + bank
                + ", ownerEmail=" + ownerEmail + ", ownerCF=" + ownerCF + "]";
    }

}
