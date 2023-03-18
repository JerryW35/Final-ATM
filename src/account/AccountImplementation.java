package src.account;

import src.Database.Models.Transaction;
import src.transaction.TransactionFactory;

import java.sql.Timestamp;
import java.util.ArrayList;

/*
Abstract AccountImplementation class

This acts as a outer covering for all the accounts, whenver a new account action has to be performed, it is done on this object which further
is passed to the Account Factory that modifies it and uses the specific object that has to be created

Think of it like creating new soda bottles (Coke, Sprite, Pepsi) first there is only an empty bottle (Account Implementation), depending on the input
the contents are put and label is added for Coke/Sprite/Pepsi(Savings, Checking, Loan or Securities).
 */

public abstract class AccountImplementation implements Account {

    private double balance;
    private int transactionLimit; // How much money can be withdrawn
    private long userId;
    private long accountNumber;
    private double interestRate;
    private double interestGenerated; //Total interest generated on this account
    private boolean debitCard; // does user have a debit card or not?
    private boolean creditCard; // does user have a credit card or not?
    private ArrayList<Transaction> transactions;
    private boolean accountLocked; // Is account locked?
    private boolean accountDeleted; // Is account deleted?
    private Timestamp createdOn; // Date account was creaated on
    private Timestamp deletedOn; // Date account was deleted on
    private String type;
    // TODO currency  symbol


    String accountCurrency;

    public boolean checkBalanceValue(){
        return true;
    }
    public boolean withdrawMoney(double amount){
        return true;
    }
    public boolean depositMoney(double amount){
        return true;
    }
    public double calculateInterestAmount(){
        return 0;
    }
    @Override
    public String getAccountType() {
        return this.type;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public double getInterest() {
        return this.interestRate;
    }

    @Override
    public void setInterest(double interest) {
        this.interestRate=interest;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestGenerated() {
        return interestGenerated;
    }

    public void setInterestGenerated(double interestGenerated) {
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

    public void addTransactions(TransactionFactory transactions) {
        this.transactions.add(transactions);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }
}
