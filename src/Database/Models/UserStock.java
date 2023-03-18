package src.Database.Models;

import java.sql.Timestamp;


/*
Following the MVC structure this is the design for the User Stock Model
 */
public class UserStock {
    int id;
    long userId;
    long accountNumber;
    int stockPurchased;
    int stockSold;
    double buyPrice;
    double sellPrice;

    int stockId;

    String stockName;

    Timestamp createdOn;

    Timestamp updatedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStockPurchased() {
        return stockPurchased;
    }

    public void setStockPurchased(int stockPurchased) {
        this.stockPurchased = stockPurchased;
    }

    public int getStockSold() {
        return stockSold;
    }

    public void setStockSold(int stockSold) {
        this.stockSold = stockSold;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public int getOwnedAmount() {
        return this.stockPurchased - this.stockSold;
    }
}
