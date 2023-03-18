package src.manager;

import src.Database.Controllers.*;
import src.Database.Models.Account;
import src.Database.Models.Transaction;
import src.Database.Models.User;
import src.Database.Models.UserStock;
import src.Helpers.VirtualDateHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
This class is specifically for the manager functions
Please note that most of the functions here are middle functions between the UI and the actual call to the Controllers and classes

The functions have self-defining names and return the vallues accordingly
 */


public class Manager {
    BankStockController stockController = new BankStockController();
    TransactionController transactionController = new TransactionController();

    UserStockController userStockController = new UserStockController();

    AccountController accountController = new AccountController();

    public boolean addNewStocks(String stock_symbol, String stock_name, double stock_price, String stock_change, String stock_country, int stock_available, String stock_sector, String stock_industry) {
        boolean status = false;
        status=stockController.createNewStock(stock_symbol, stock_name, stock_price, stock_change,stock_country,  stock_available,  stock_sector,  stock_industry);
        return status;
    }

    public void updateExistingStock(String stockSymbol,double stockPrice, int stockAvailable) {
        stockController.updateStock(stockSymbol, stockPrice); // change price
        stockController.updateStock(stockSymbol, stockAvailable); // change available
    }
    public ArrayList<Transaction> getDailyTransactions(Date date){
        return transactionController.getAllTransactionsOnDate(date);
    }

    public ArrayList<UserStock> getDailyStocks(Date date){
        return userStockController.getAllStocksOnDate(date);
    }

    public ArrayList<Account> getDailyAccounts(Date date){
        return accountController.getAllAccountsOnDate(date);
    }

    public int getDailyTransactionsCount(Date date){
        System.out.println(date);
        return transactionController.getAllTransactionsOnDateCount(date);
    }

    public int getDailyStocksCount(Date date){
        return userStockController.getAllAccountsOnDateCount(date);
    }

    public int getDailyAccountsCount(Date date){
        return accountController.getAllAccountsOnDateCount(date);
    }
    public void generateTransactionReport(String outputFile) throws IOException {
        HashMap<Long, ArrayList<Transaction>> allTransaction= transactionController.getTransactionsByAccount();
        for (ArrayList<Transaction> transactions: allTransaction.values()) {
            // write to csv.file by java
        }
    }

    public void virtualDateChange(){
        VirtualDateHandler.changeDate();
        Date currentDate = VirtualDateHandler.currentDate;
        AccountController accountController = new AccountController();
        accountController.getInterestFromLoanAccounts(currentDate);
        accountController.addInterestToAccounts(currentDate);
    }

    public ArrayList<User> getListOfPoorUsers(){
        UserController userController = new UserController();
        HashMap<String, User> users = userController.getAllUsers();
        ArrayList<User> poorUsers = new ArrayList<>();
        users.forEach((s, user) -> {
            if(!user.getManager()) {
                if (userController.getAggregateBalanceOfUser(user.getId()) < 2000 || accountController.checkIfInterestUnpaidOnLoan(user.getId())) {
                    poorUsers.add(user);
                }
            }
        });

        return poorUsers;
    }

    public ArrayList<Date> getListOfDatesForFilter(){
        ArrayList<Date> dates = accountController.getListOfDatesInTable();
        dates.addAll(transactionController.getListOfDatesInTable());
        dates.addAll(userStockController.getListOfDatesInTable());

        return dates;
    }
}

