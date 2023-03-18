package src.account;

import src.Database.Controllers.AccountController;
import src.Database.Controllers.AccountInterestController;
import src.Database.Controllers.TransactionController;
import src.Database.Models.Account;
import src.account.accountType.Checking;
import src.account.accountType.Loan;
import src.account.accountType.Savings;
import src.account.accountType.Securities;

/*
Account factory class. Here is where the factory magic happens
createAccount function takes an Account object which by matching what type the object has is created as either Savings, Checking or Loan or Securities
we have also defined a getByTypeOfAccount generic function that helps in returning the specific object of the type depending on the inner value of account type

This factory also adds money to destination account (Reason that there is this function here even though a deposit function is present in each specific class
is that this function decides which specific type of object is to be called when calling their deposit function)

Works like a factory where based on the input the label is put on the bottle (account)
 */
public class AccountFactory {
    public boolean createAccount(Account account) {
        //TODO: db operation
        AccountController accountController = new AccountController();
        AccountInterestController accountInterestController = new AccountInterestController();
        if(account.getType().equalsIgnoreCase("savings")){
            if(account.getBalance()>2000){
                account.setInterestRate(accountInterestController.returnAccountInterestRate(account.getType()));
            }else{
                account.setInterestRate(0);
            }
        }else if(account.getType().equalsIgnoreCase("loan")){
            account.setInterestRate(accountInterestController.returnAccountInterestRate(account.getType()));
        }
        accountController.createNewAccount(account.getType(), account.getBalance(), account.getUserId(), account.getTransactionLimit(), account.getInterestRate(), true, true, account.getAccountCurrency());
        return true;
    }

    public boolean closeAccount(long accountNumber){
        AccountController accountController = new AccountController();
        accountController.deleteAccount(accountNumber);
        return true;
    }

    public static AccountImplementation getByType(String type) {
        switch (type.toLowerCase()) {
            case "savings":
                return new Savings();
            case "checking":
                return new Checking();
            case "securities":
                return new Securities();
            case "loan":
                return new Loan();
            default:
                return null;
        }
    }

    public <T extends AccountImplementation> T getByTypeOfAccount(String type){
        switch (type.toLowerCase()) {
            case "savings":
                return (T) new Savings();
            case "checking":
                return (T) new Checking();
            case "securities":
                return (T) new Securities();
            case "loan":
                return (T) new Loan();
            default:
                return null;
        }
    }

    //add a function to transfer money to all accounts

    public static void addMoneyToDestinationAccount(double amount, long accountNumber){
        AccountController accountController = new AccountController();
        Account account = accountController.getAccountByNumber(accountNumber);
        TransactionController transactionController = new TransactionController();
        transactionController.createNewTransaction(amount, "Credit", "Deposit", accountNumber, account.getUserId());
        accountController.updateAccountBalance(accountNumber, account.getBalance()+amount);
    }
}
