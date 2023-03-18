package src.account.accountType;

import src.Database.Controllers.AccountController;
import src.Database.Models.Transaction;
import src.account.AccountImplementation;
import src.StockTrading.Stock;
import src.transaction.AccountTransaction;
import src.transaction.FeeTransaction;

import java.sql.Timestamp;
import java.util.ArrayList;

/*
This class is the extention of the abstract class Account Implementation
This class is an implementation of a factory.
Factory follows the structure as follows:-
Account (Interface Class)
AccountImplementation (Abstract class implementing Account by giving definitions of functions that every account must implement)
Types of Product in factory extends Account Implementation
1. Checking
2. Loan
3. Savings
4. Securities

Account Factory creates the products according to use requirement which is type of account being created

Checking account implements the following functions for its own use which are different from AccountImplementation

1. calculateInterestAmount funciton to claculate interested on this specific account
 */

public class Securities extends AccountImplementation {

    private double balance;
    private int transactionLimit; // How much money can be withdrawn
    private long userId;
    private long accountNumber;
    private double interestRate;
    private double interestGenerated; //Total interest generated on this account
    private boolean debitCard; // does user have a debit card or not?
    private boolean creditCard; // does user have a credit card or not?
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private boolean accountLocked; // Is account locked?
    private boolean accountDeleted; // Is account deleted?
    private Timestamp createdOn; // Date account was creaated on
    private Timestamp deletedOn; // Date account was deleted on
    private String type="Securities";
    private ArrayList<Stock> stocks = new ArrayList<Stock>();
    private int profit = 0;
    private AccountController controller =  new AccountController();

    public void generateProfitOrLoss(){
        // TODO : Generate profit or loss
        for(Stock stock: stocks){
            profit+= stock.getStockInterest()*stock.getHavingShares()*stock.getSalePrice();
        }
    }
    public int getSalePrice(Stock stock , int numberOfShares){
        return stock.getSalePrice()*numberOfShares;
    }
    public int getBuyPrice(Stock stock , int numberOfShares){
        return stock.getBuyPrice()*numberOfShares;
    }


    public boolean checkBalanceValue(){
        return this.balance>0;
    }
    public boolean withdrawMoney(double amount){
        if(this.checkBalanceValue()&&this.balance>=amount){
            controller.updateAccountBalance(this.accountNumber, this.balance);


            AccountTransaction newTransaction = new AccountTransaction(accountNumber, "withdraw",new Timestamp(System.currentTimeMillis()) , amount,"Withdraw");
            FeeTransaction newFee= new FeeTransaction(accountNumber, 0.001*amount, new Timestamp(System.currentTimeMillis()), "withdraw money from security account");
            Transaction transaction =newTransaction.getTransactionData();
            Transaction feeTransaction = newFee.getTransactionData();
            this.transactions.add(transaction);
            this.transactions.add(feeTransaction);
            return true;
        }
        return false;
    }
    public boolean depositMoney(double amount){
        if(amount<1000) return false;
        controller.updateAccountBalance(this.accountNumber, this.balance);

        AccountTransaction newTransaction = new AccountTransaction(accountNumber, "deposit",new Timestamp(System.currentTimeMillis()) , -1*amount,"Deposit");
        Transaction transaction = newTransaction.getTransactionData();
        this.transactions.add(transaction);
        return true;
    }
    public double calculateInterestAmount(){
        double interest=getInterest();
        double interestAmount=getBalance();
        return interestAmount*interest*(1/12);
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

    public void addTransactions(Transaction transactions) {
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
    public int getProfit() {
        return profit;
    }
    public void setProfit(int profit) {
        this.profit = profit;
    }
}
