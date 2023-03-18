package src.Database.Models;

import java.sql.Timestamp;
import java.util.ArrayList;

/*
Following the MVC structure this is the design for the Account Model
 */

public class Account {

    //Add currency here and in database
    private long accountNumber;
    private double balance;
    private long userId;
    private boolean accountLocked; // Is account locked?
    private boolean accountDeleted; // Is account deleted?
    private String type;

    String accountCurrency;


    private double transactionLimit; // How much money can be withdrawn
    private double interestRate;
    private double interestGenerated; //Total interest generated on this account
    private boolean debitCard; // does user have a debit card or not?
    private boolean creditCard; // does user have a credit card or not?
    private ArrayList<Transaction> transactions;

    private Timestamp createdOn; // Date account was created on
    private Timestamp deletedOn; // Date account was deleted on

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAccountDeleted() {
        return accountDeleted;
    }

    public void setAccountDeleted(boolean accountDeleted) {
        this.accountDeleted = accountDeleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public double getInterestGenerated() {
        return interestGenerated;
    }

    public void setInterestGenerated(double interestGenerated) {
        this.interestGenerated = interestGenerated;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public void setInterestGenerated(int interestGenerated) {
        this.interestGenerated = interestGenerated;
    }

    public boolean isDebitCard() {
        return debitCard;
    }

    public void setDebitCard(boolean debitCard) {
        this.debitCard = debitCard;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Timestamp deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }
}
