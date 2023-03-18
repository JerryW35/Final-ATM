package src.account.accountType;

import src.Database.Controllers.AccountController;
import src.Database.Controllers.LoanController;
import src.Database.Models.LoanModel;
import src.Database.Models.Transaction;
import src.Helpers.DateConverter;
import src.account.AccountFactory;
import src.account.AccountImplementation;
import src.transaction.AccountTransaction;
import src.transaction.FeeTransaction;
import src.transaction.LoanTransaction;
import src.transaction.TransactionFactory;

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

Loan account implements the following functions for its own use which are different from AccountImplementation

1. takeLoan : This is specific to the loan account as it allows taking loan by charging an interest
 */

public class Loan extends AccountImplementation {

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
    private String type;
    private double sanctionAmount;  // can take loan of only this much amount
    private AccountController controller =  new AccountController();


    public boolean checkBalanceValue(){
        return this.balance>0;
    }
    public boolean withdrawMoney(double amount){
        if(this.checkBalanceValue()&&this.balance>=amount){
            this.balance-=amount;
            controller.updateAccountBalance(this.accountNumber, this.balance);


            AccountTransaction newTransaction = new AccountTransaction(accountNumber, "withdraw",new Timestamp(System.currentTimeMillis()) , amount,"Withdrawal");
            Transaction transaction = newTransaction.getTransactionData();
            FeeTransaction newFee= new FeeTransaction(accountNumber, 0.001*amount, new Timestamp(System.currentTimeMillis()), "withdraw money from loan account");
//            this.transactions.add(transaction);
//            this.transactions.add(newFee);
            return true;
        }
        return false;
    }
    public boolean depositMoney(double amount){
        this.balance+=amount;
        controller.updateAccountBalance(this.accountNumber, this.balance);

        AccountTransaction newTransaction = new AccountTransaction(accountNumber, "deposit",new Timestamp(System.currentTimeMillis()) , amount,"Deposit");
        Transaction transaction = newTransaction.getTransactionData();
        this.transactions.add(transaction);
        return true;
    }
    public double calculateInterestAmount(){
        double interest=getInterest();
        double interestAmount=getBalance();
        return -1*interestAmount*interest*(1/12);
    }
    public boolean takeLoan(double amount, String purpose, long destinationAccount){
        System.out.println("here now");
        // check if amount less than sanctionedAmount
        if(amount<=this.balance){
            LoanTransaction newTransaction = new LoanTransaction(accountNumber,createdOn,deletedOn,amount,interestRate,"take loan");
            AccountFactory.addMoneyToDestinationAccount(amount, destinationAccount);
            LoanController loanController = new LoanController();
            LoanModel loanModel = new LoanModel();
            loanModel.setUserId(this.userId);
            loanModel.setAccountNumber(this.accountNumber);
            loanModel.setLoanAmount(amount);
            loanModel.setLoanPurpose(purpose);
            loanModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            loanModel.setDueDate(new Timestamp(System.currentTimeMillis()+ DateConverter.MILLIS_IN_A_YEAR));
            AccountController accountController = new AccountController();
            loanModel.setInterestAmount(loanModel.getLoanAmount()*accountController.getAccountByNumber(accountNumber).getInterestRate());
            accountController.updateAccountBalance(this.accountNumber, this.balance-amount);
            if(accountController.checkIfAccountExists(destinationAccount)) {
                accountController.updateAccountBalance(destinationAccount, accountController.getAccountByNumber(destinationAccount).getBalance() + amount);
            }
            loanController.createNewLoan(loanModel);
            FeeTransaction newFee= new FeeTransaction(accountNumber, 0.001*amount, createdOn, "loan money from loan account");
//            Transaction transaction = newTransaction.getTransactionData();
//            Transaction feeTransaction = newFee.getTransactionData();
//            this.transactions.add(transaction);
//            this.transactions.add(feeTransaction);
            return true;
        }
        return false;
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

    public void addTransactions(TransactionFactory transactions) {
        this.transactions.add(transactions);
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

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
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
}
