package src.StockTrading;
import src.Database.Controllers.AccountController;
import src.Database.Controllers.BankStockController;
import src.Database.Models.Transaction;
import src.transaction.StockTransaction;

import java.sql.Timestamp;

/*
A command pattern has been implemented to handle stocks for any user
The Stock commands involves executing specific functions as given on the interface to execute buying and selling of shares by any user
 */
public class StockCommands implements StockCommandsInterface {
    AccountController controller = new AccountController();
    BankStockController bankStockController = new BankStockController();
    public boolean executeBuyShares(int stockId, long accountNumber, int numberOfShares){
        boolean status = false;
        if(controller.getAccountByNumber(accountNumber).getType().equalsIgnoreCase("Securities") && controller.getAccounts().get(accountNumber).getBalance()>2500 && numberOfShares<bankStockController.getSpecificStock(stockId).getStockAvailable()){
            bankStockController.buyStock(accountNumber, numberOfShares, bankStockController.getSpecificStock(stockId));
            status =true;
            // store into transaction
            StockTransaction newTransaction = new StockTransaction(accountNumber, true, bankStockController.getSpecificStock(stockId).getStockName(), numberOfShares, bankStockController.getSpecificStock(stockId).getStockPrice(), new Timestamp(System.currentTimeMillis()), "Buy Shares");
            Transaction generatedTransaction = newTransaction.getTransactionData();
            controller.getAccountByNumber(accountNumber).getTransactions().add(generatedTransaction);
        }
        return status;
    }
    public boolean executeSellShares(int stockId, long accountNumber, int numberOfShares) {
        boolean status = false;
        if (controller.getAccountByNumber(accountNumber).getType().equalsIgnoreCase("Securities") && numberOfShares < bankStockController.getSpecificStock(stockId).getStockAvailable()) {
            bankStockController.sellStock(accountNumber,numberOfShares,bankStockController.getSpecificStock(stockId),bankStockController.getSpecificStock(stockId).getStockPrice());
            status = true;
            // store into transaction
            StockTransaction newTransaction = new StockTransaction(accountNumber, false, bankStockController.getSpecificStock(stockId).getStockName(), numberOfShares, bankStockController.getSpecificStock(stockId).getStockPrice(), new Timestamp(System.currentTimeMillis()), "Sell Shares");
            Transaction generatedTransaction = newTransaction.getTransactionData();
            controller.getAccountByNumber(accountNumber).getTransactions().add(generatedTransaction);

        }
        return status;
    }
}
