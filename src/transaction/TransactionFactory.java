package src.transaction;

import org.apache.commons.beanutils.BeanUtils;
import src.Database.Controllers.AccountController;
import src.Database.Controllers.BankStockController;
import src.Database.Models.Account;
import src.Database.Models.BankStock;
import src.Database.Models.Transaction;
import src.account.accountType.Loan;

import java.lang.reflect.InvocationTargetException;
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

public class TransactionFactory extends Transaction implements TransactionInterface {
    public void processTransfer(long accountNumber, long targetAccountNumber, double amount, String narration){
        AccountController accountController = new AccountController();
        Account account = accountController.getAccountByNumber(accountNumber);
        Account targetAccount = accountController.getAccountByNumber(targetAccountNumber);
        System.out.println("making transaction");
        switch (account.getType().toLowerCase()){
            case "checking":
                System.out.println("checking");
                AccountTransaction checkingAccountTransaction = new AccountTransaction(account.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), amount, narration);
                if(targetAccount !=null) {
                    AccountTransaction checkingTargetAccountTransaction = new AccountTransaction(targetAccount.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), (amount * -1), narration);
                }
                break;
            case "savings":
                System.out.println("savings");
                AccountTransaction savingsAccountTransaction = new AccountTransaction(account.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), amount, narration);
                if(targetAccount !=null) {
                    AccountTransaction savingsTargetAccountTransaction = new AccountTransaction(targetAccount.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), (amount * -1), narration);
                }
                break;
            case "securities":
                System.out.println("securities");
                AccountTransaction securitiesAccountTransaction = new AccountTransaction(account.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), amount, narration);
                if(targetAccount !=null) {
                    AccountTransaction securitiesTargetAccountTransaction = new AccountTransaction(targetAccount.getAccountNumber(), "transfer", new Timestamp(System.currentTimeMillis()), (amount * -1), narration);
                }
                break;
        }
    }

    public void processTransfer(long accountNumber, int shares, String narration, boolean buySell, String stockSymbol){
        AccountController accountController = new AccountController();
        Account account = accountController.getAccountByNumber(accountNumber);
        switch (account.getType().toLowerCase()){
            case "securities":
                BankStockController bankStockController = new BankStockController();
                BankStock bankStock = bankStockController.getSpecificStock(stockSymbol);
                StockTransaction stockTransaction = new StockTransaction(account.getAccountNumber(), buySell, bankStock.getStockName(), shares, bankStock.getStockPrice(), new Timestamp(System.currentTimeMillis()), "Buying stock");
                break;
        }
    }

    public boolean processTransfer(long accountNumber, double amount, String purpose, long destinationAccount){
        AccountController accountController = new AccountController();
        Account account = accountController.getAccountByNumber(accountNumber);
        switch (account.getType().toLowerCase()){
            case "loan":
                Loan loan = new Loan();
                try {
                    BeanUtils.copyProperties(loan, account);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return loan.takeLoan(amount, purpose, destinationAccount);
        }
        return false;
    }
}
