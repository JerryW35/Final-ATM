package src.Database.Controllers;

import src.Database.Models.Account;
import src.Database.Models.BankStock;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.HashMap;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class BankStockController {
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<String, BankStock> stocks = new HashMap<>();
    HashMap<Integer, BankStock> stocksById = new HashMap<>();

    public boolean createNewStock(String stock_symbol, String stock_name, double stock_price, String stock_change, String stock_country, int stock_available, String stock_sector, String stock_industry) {
        if ( stock_symbol.length() == 0 || stock_name.length() == 0 || stock_price < 0
            || stock_change.length() == 0 || stock_country.length() == 0 || stock_available < 0
            || stock_sector.length() == 0 || stock_industry.length() == 0
        ) {
            throw new RuntimeException();
        }

        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Timestamp updateAt = createdAt;
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `stock`(stock_symbol, stock_name, stock_price, stock_change, stock_country, stock_available, stock_sector, stock_industry) VALUE (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, stock_symbol);
            pstmt.setString(2, stock_name);
            pstmt.setDouble(3, stock_price);
            pstmt.setString(4, stock_change);
            pstmt.setString(5, stock_country);
            pstmt.setInt(6, stock_available);
            pstmt.setString(7, stock_sector);
            pstmt.setString(8, stock_industry);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public boolean getAllStocks() {
        stocks.clear();


        String sql = "select * from stock";
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Condition check
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            BankStock stock = new BankStock();

            try {
                stock.setId(rs.getInt("stock_id"));
                stock.setStockName(rs.getString("stock_name"));
                stock.setStockAvailable(rs.getInt("stock_available"));
                stock.setStockChange(rs.getString("stock_change"));
                stock.setStockCountry(rs.getString("stock_country"));
                stock.setStockIndustry(rs.getString("stock_industry"));
                stock.setStockSymbol(rs.getString("stock_symbol"));
                stock.setStockPrice(rs.getDouble("stock_price"));
                stock.setStockSector(rs.getString("stock_sector"));

                stocks.put(stock.getStockSymbol(), stock);
                stocksById.put(stock.getId(), stock);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public HashMap<String, BankStock> getAllAvailableStocks(){
        getAllStocks();
        return stocks;
    }


    public BankStock getSpecificStock(String stockSymbol){
        getAllStocks();
        return stocks.get(stockSymbol);
    }

    public double getBuyingPriceForSharesSelected(int shareCount, String stockSymbol){
        BankStock bankStock = getSpecificStock(stockSymbol);
        return bankStock.getStockPrice()*shareCount;
    }

    public double getPredictedGrowthForSharesSelected(int shareCount, String stockSymbol){
        BankStock bankStock = getSpecificStock(stockSymbol);
        double buyingPrice = getBuyingPriceForSharesSelected(shareCount, stockSymbol);
        double predictedValue = buyingPrice*(Double.parseDouble(bankStock.getStockChange().replace("%", ""))/100);
        return predictedValue;
    }

    public BankStock getSpecificStock(int stockId){
        getAllStocks();
        return stocksById.get(stockId);
    }

    public void updateStock(String stockSymbol, int sharesBought){
        getAllStocks();
        BankStock stock = stocks.get(stockSymbol);
        if(stocks.containsKey(stockSymbol)){
            if(sharesBought<=stock.getStockAvailable()) {
                stock.setStockAvailable(stock.getStockAvailable() - sharesBought);
                sqlConnection.makeConnection();
                Connection connection = sqlConnection.getConnection();
                PreparedStatement pstmt = null;
                try {
                    pstmt = connection.prepareStatement("UPDATE stock SET stock_available=? WHERE stock_symbol=?");
                    pstmt.setInt(1, stock.getStockAvailable());
                    pstmt.setString(2, stockSymbol);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateStock(String stockSymbol, double stockPrice){
        getAllStocks();
        BankStock stock = stocks.get(stockSymbol);
        if(stocks.containsKey(stockSymbol)){
            stock.setStockPrice(stockPrice);
            sqlConnection.makeConnection();
            Connection connection = sqlConnection.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("UPDATE stock SET stock_price=? WHERE stock_symbol=?");
                pstmt.setDouble(1, stockPrice);
                pstmt.setString(2, stockSymbol);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateStockAvailable(String stockSymbol, int stockAvailable){
        getAllStocks();
        BankStock stock = stocks.get(stockSymbol);
        if(stocks.containsKey(stockSymbol)){
            stock.setStockAvailable(stockAvailable);
            sqlConnection.makeConnection();
            Connection connection = sqlConnection.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("UPDATE stock SET stock_available=? WHERE stock_symbol=?");
                pstmt.setInt(1, stockAvailable);
                pstmt.setString(2, stockSymbol);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean buyStock(long accountNumber, int purchaseQuantity, BankStock stock){
        AccountController accountController = new AccountController();
        Account purchasingAccount = accountController.getAccountByNumber(accountNumber);
        UserStockController userStockController = new UserStockController();
        if(purchasingAccount.getBalance()>(purchaseQuantity*stock.getStockPrice())) {
            userStockController.createNewUserStock(purchasingAccount.getUserId(), accountNumber, purchaseQuantity, stock.getStockPrice(), stock.getId());
            purchasingAccount.setBalance(purchasingAccount.getBalance()-(purchaseQuantity*stock.getStockPrice()));
            accountController.updateAccountBalance(purchasingAccount.getAccountNumber(), purchasingAccount.getBalance());
            return true;
        }else{
            return false;
        }
    }

    public void sellStock(long accountNumber, int saleQuantity, BankStock stock, double salePrice){
        AccountController accountController = new AccountController();
        Account purchasingAccount = accountController.getAccountByNumber(accountNumber);
        UserStockController userStockController = new UserStockController();
        userStockController.makeSaleOfStock(saleQuantity, stock.getId(), (stock.getStockAvailable()-saleQuantity), salePrice);
        purchasingAccount.setBalance(purchasingAccount.getBalance() + salePrice*saleQuantity);
        accountController.updateAccountBalance(purchasingAccount.getAccountNumber(), purchasingAccount.getBalance());
    }
}
