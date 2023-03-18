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
public class InterestTransaction extends TransactionFactory {
    private long accountNumber;
    private String transactionType="interest";
    private Timestamp transactionDate;
    private String narration;
    private double transactionAmount;
    public InterestTransaction(long accountNumber, Timestamp transactionDate, double transactionAmount, String narration) {
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.narration = narration;
        this.transactionAmount = transactionAmount;
        TransactionController transactionController = new TransactionController();
        AccountController accountController = new AccountController();

        transactionController.createNewTransaction(transactionAmount, "Debit", "Interest charged on transaction", accountNumber, accountController.getAccountByNumber(accountNumber).getUserId());

    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
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
}
