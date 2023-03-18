package src.StockTrading;


/*
A command pattern has been implemented to handle stocks for any user
The Stock commands involves executing specific functions as given on the interface to execute buying and selling of shares by any user
 */
public interface StockCommandsInterface {
    public boolean executeBuyShares(int stockId, long accountNumber, int numberOfShares);
    public boolean executeSellShares(int stockId, long accountNumber, int numberOfShares);
}
