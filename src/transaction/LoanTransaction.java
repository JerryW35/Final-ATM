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
public class LoanTransaction extends TransactionFactory {
    private long accountNumber;
    private String transactionType="loan";
    private Timestamp transactionDate; //start date of loan
    private Timestamp dueDate;         //end date of loan
    private double loanAmount;
    private double interestRate;
    private String narration;

    public LoanTransaction(long accountNumber, Timestamp transactionDate, Timestamp dueDate, double loanAmount, double interestRate, String narration) {
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.dueDate = dueDate;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.narration = narration;

        TransactionController transactionController = new TransactionController();
        AccountController accountController = new AccountController();

        transactionController.createNewTransaction(loanAmount, "Debit", "Loan withdrawn", accountNumber, accountController.getAccountByNumber(accountNumber).getUserId());

    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public Transaction getTransactionData(){
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(this.accountNumber);
        transaction.setTxnType(this.transactionType);
        transaction.setTxnDate(this.transactionDate);
        transaction.setTxnNarration(this.narration);
        transaction.setTxnAmount(this.loanAmount);
        return transaction;
    }
}
