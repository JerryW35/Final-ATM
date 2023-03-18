package src.transaction;

import src.Database.Controllers.AccountController;
import src.Database.Controllers.TransactionController;
import src.Database.Models.Transaction;

import java.sql.Timestamp;

/*
Transaction Factory implements the factory pattern for transactions
Based on the input provided by the user for which account they are performing the transaction on,
the factory returns the specific transaction to process the transfer. Different types of transactions are as follows:
    - Account Transaction
        This allows for between account transfers to helps us make internal transfers
    - Fee Transactions
        This is a transaction generated for every fee that is present so as to allow for a transaction to take place
    - Interest Transaction
    - Loan Transaction
    - Stock transaction
 */

public class AccountTransaction extends TransactionFactory {
    private long accountNumber;
    private String transactionType;  // withdraw or deposit
    private Timestamp transactionDate;
    private double transactionAmount;
    private String narration;

    public AccountTransaction(long accountNumber, String withdraw, Timestamp timestamp, double amount, String narration) {
        this.accountNumber = accountNumber;
        this.transactionType = withdraw;
        this.transactionDate = timestamp;
        this.transactionAmount = amount;
        this.narration = narration;
        AccountController accountController = new AccountController();
        accountController.updateAccountBalance(accountNumber, accountController.getAccountByNumber(accountNumber).getBalance()-amount);
        createTransaction(accountNumber, accountController.getAccountByNumber(accountNumber).getUserId());
    }
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public Transaction getTransactionData(){
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(this.accountNumber);
        transaction.setTxnType(this.transactionType);
        transaction.setTxnDate(this.transactionDate);
        transaction.setTxnNarration(this.narration);
        transaction.setTxnAmount(this.transactionAmount);
        return transaction;
    }

    private void createTransaction(long accountNumber, long userId){
        TransactionController transactionController = new TransactionController();
        transactionController.createNewTransaction(this.transactionAmount, this.transactionType, this.narration, accountNumber, userId);
    }
}
