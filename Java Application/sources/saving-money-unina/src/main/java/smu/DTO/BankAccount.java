package smu.DTO;


public class BankAccount {

    //Attributes
    private Integer balance;
    private Integer accountNumber;
    private String bank;
    private String ownerEmail;
    private String ownerCF;

    //Constructor
    public BankAccount(Integer balance, Integer accountNumber, String bank, String ownerEmail, String ownerCF) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.bank = bank;
        this.ownerEmail = ownerEmail;
        this.ownerCF = ownerCF;
    }

    //Getters and Setters

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
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
