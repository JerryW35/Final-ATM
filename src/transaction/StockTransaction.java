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

public class StockTransaction extends TransactionFactory {
    private long accountNumber;
    private String transactionType="stock";
    private boolean buyOrSell; //true for buy, false for sell
    private String stockName;
    private int sharesAmount;
    private double stockPrice;
    private double transactionAmount; // total amount of buying or selling stocks

    private String narration;
    private Timestamp transactionDate;

    public Timestamp getTransactionDate() {
        return transactionDate;
    }
    public StockTransaction(long accountNumber, boolean buyOrSell, String stockName, int sharesAmount, double stockPrice, Timestamp transactionDate, String narration) {
        this.accountNumber = accountNumber;
        this.buyOrSell = buyOrSell;
        this.stockName = stockName;
        this.sharesAmount = sharesAmount;
        this.stockPrice = stockPrice;
        this.transactionDate = transactionDate;
        this.transactionAmount = sharesAmount * stockPrice;
        this.narration = narration;

        TransactionController transactionController = new TransactionController();
        AccountController accountController = new AccountController();

        if(buyOrSell){
            transactionController.createNewTransaction(transactionAmount, "Debit", "Stock purchased", accountNumber, accountController.getAccountByNumber(accountNumber).getUserId());
        }else{
            transactionAmount = transactionAmount*-1;
            transactionController.createNewTransaction(transactionAmount, "Credit", "Stock sold", accountNumber, accountController.getAccountByNumber(accountNumber).getUserId());
        }

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

    public boolean isBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(boolean buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getSharesAmount() {
        return sharesAmount;
    }

    public void setSharesAmount(int sharesAmount) {
        this.sharesAmount = sharesAmount;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
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
