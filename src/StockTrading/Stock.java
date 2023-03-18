package src.StockTrading;

/*
This is the stock view in accordance with the MVC architecture
 */
public class Stock {
    private int stockId;
    private String stockName;
    private String stockSymbol;
    private int buyPrice;
    private int salePrice;
    private double stockInterest;

    private int havingShares;
    private boolean stockPerformance; // 1 if stock is performing well and gives profit or else 0
    private long marketShares; // how many shares are available of this stock in the market


    public void setHavingShares(int havingShares) {
        this.havingShares = havingShares;
    }

    public int getHavingShares() {
        return havingShares;
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

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public double getStockInterest() {
        return stockInterest;
    }
    public void setStockInterest(double stockInterest) {
        this.stockInterest = stockInterest;
    }
    public boolean isStockPerformance() {
        return stockPerformance;
    }
    public void setStockPerformance(boolean stockPerformance) {
        this.stockPerformance = stockPerformance;
    }
    public long getMarketShares() {
        return marketShares;
    }
    public void setMarketShares(long marketShares) {
        this.marketShares = marketShares;
    }


}
