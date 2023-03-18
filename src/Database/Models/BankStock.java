package src.Database.Models;



/*
Following the MVC structure this is the design for the Bank Stock Model
 */
public class BankStock {
    int id;
    String stockSymbol;
    String stockName;
    double stockPrice;
    String stockChange;
    String stockCountry;
    int stockAvailable;
    String stockSector;
    String stockIndustry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getStockChange() {
        return stockChange;
    }

    public void setStockChange(String stockChange) {
        this.stockChange = stockChange;
    }

    public String getStockCountry() {
        return stockCountry;
    }

    public void setStockCountry(String stockCountry) {
        this.stockCountry = stockCountry;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public String getStockSector() {
        return stockSector;
    }

    public void setStockSector(String stockSector) {
        this.stockSector = stockSector;
    }

    public String getStockIndustry() {
        return stockIndustry;
    }

    public void setStockIndustry(String stockIndustry) {
        this.stockIndustry = stockIndustry;
    }
}
