package src.Database.Controllers;

import src.Database.Models.UserStock;
import src.Database.Models.UserStocks;
import src.Helpers.DateConverter;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class UserStockController {
    SQLConnection sqlConnection = new SQLConnection();
    BankStockController bankStockController = new BankStockController();

    HashMap<Long, UserStocks> userStocks = new HashMap<>();

    ArrayList<UserStock> stocks = new ArrayList<>();
    public boolean createNewUserStock(long userId, long accountNumber, int stockPurchased, double buyPrice, int stockId){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        AccountController accountController = new AccountController();
        if(!accountController.checkIfAccountsExist(userId)) {
            try {
                Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                Timestamp updateAt = createdAt;
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `user_stock`(user_id,account_no,stock_purchased,buy_price, stock_id, created_at, updated_at) VALUE (?,?,?,?,?,?,?)");
                pstmt.setLong(1, userId);
                pstmt.setLong(2, accountNumber);
                pstmt.setInt(3, stockPurchased);
                pstmt.setDouble(4, buyPrice);
                pstmt.setInt(5, stockId);
                pstmt.setTimestamp(6, createdAt);
                pstmt.setTimestamp(7, updateAt);

                int returnValue = pstmt.executeUpdate();

                if (returnValue > 0) {
                    bankStockController.updateStock(bankStockController.getSpecificStock(stockId).getStockSymbol(), stockPurchased);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else{
            return false;
        }

        return true;
    }

    public void makeSaleOfStock(int stockSold, int stockId, int stockAvailable, double salePrice){
        try {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Connection connection = sqlConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement("UPDATE user_stock SET stock_sold = ?, stock_purchased=?, sale_price=?, updated_at=? WHERE id=? and stock_purchased>0");
            pstmt.setInt(1, stockSold);
            pstmt.setInt(2, stockAvailable);
            pstmt.setDouble(3, salePrice);
            pstmt.setTimestamp(4, createdAt);
            pstmt.setInt(5, stockId);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                bankStockController.updateStock(bankStockController.getSpecificStock(stockId).getStockSymbol(), (-1*stockSold));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllUserStocks(){
        stocks.clear();

        String sql = "select * from user_stock";
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
            try{
                    UserStock userStock = new UserStock();
                    userStock.setUserId(rs.getLong("user_id"));
                    userStock.setId(rs.getInt("id"));
                    userStock.setAccountNumber(rs.getLong("account_no"));
                    userStock.setBuyPrice(rs.getDouble("buy_price"));
                    userStock.setSellPrice(rs.getDouble("sale_price"));
                    userStock.setStockSold(rs.getInt("stock_sold"));
                    userStock.setStockPurchased(rs.getInt("stock_purchased"));
                    userStock.setStockId(rs.getInt("stock_id"));
                userStock.setCreatedOn(rs.getTimestamp("created_at"));
                userStock.setUpdatedOn(rs.getTimestamp("updated_at"));
                    stocks.add(userStock);
            } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    public UserStocks getUserStocks(long userId){
        userStocks.clear();

        String sql = "select * from user_stock WHERE user_id=?";
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, userId);
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

            if(userStocks.containsKey(userId)){
                UserStocks userStocksStored = userStocks.get(userId);
                try {
                    int stockToBePut = rs.getInt("stock_id");
                    if(userStocksStored.getUserStocks().containsKey(stockToBePut)){
                       UserStock userStock = userStocksStored.getUserStocks().get(stockToBePut);
                       userStock.setStockPurchased(userStock.getStockPurchased()+rs.getInt("stock_purchased"));
                    }else{
                        UserStock userStock = new UserStock();
                        userStock.setUserId(userId);
                        userStock.setId(rs.getInt("id"));
                        userStock.setAccountNumber(rs.getLong("account_no"));
                        userStock.setBuyPrice(rs.getDouble("buy_price"));
                        userStock.setSellPrice(rs.getDouble("sale_price"));
                        userStock.setStockSold(rs.getInt("stock_sold"));
                        userStock.setStockPurchased(rs.getInt("stock_purchased"));
                        userStock.setStockId(rs.getInt("stock_id"));
                        userStock.setCreatedOn(rs.getTimestamp("created_at"));
                        userStock.setUpdatedOn(rs.getTimestamp("updated_at"));
                        userStocksStored.getUserStocks().put(stockToBePut, userStock);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    UserStocks userStocksToStore = new UserStocks();
                    int stockToBePut = rs.getInt("stock_id");
                    UserStock userStock = new UserStock();
                    userStock.setUserId(userId);
                    userStock.setId(rs.getInt("id"));
                    userStock.setAccountNumber(rs.getLong("account_no"));
                    userStock.setBuyPrice(rs.getDouble("buy_price"));
                    userStock.setSellPrice(rs.getDouble("sale_price"));
                    userStock.setStockSold(rs.getInt("stock_sold"));
                    userStock.setStockPurchased(rs.getInt("stock_purchased"));
                    userStock.setStockId(rs.getInt("stock_id"));
                    userStock.setCreatedOn(rs.getTimestamp("created_at"));
                    userStock.setUpdatedOn(rs.getTimestamp("updated_at"));
                    userStocksToStore.setUserId(userId);
                    userStocksToStore.getUserStocks().put(stockToBePut, userStock);
                    userStocks.put(userId, userStocksToStore);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return getUserStocks().get(userId);
    }

    private HashMap<Long, UserStocks> getUserStocks() {
        return userStocks;
    }

    public ArrayList<UserStock> getAllStocksOnDate(Date currentDate){
        getAllUserStocks();
        Long time = currentDate.getTime();
        time = time - time % (24 * 60 * 60 * 1000);
        Long finalTime = time;
        return stocks.stream().filter(stock -> stock.getCreatedOn().after(new Timestamp(finalTime)) && stock.getCreatedOn().before(new Timestamp(finalTime + DateConverter.MILLIS_IN_A_DAY)))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public int getAllAccountsOnDateCount(Date currentDate){
        return getAllStocksOnDate(currentDate).size();
    }


    public ArrayList<Date> getListOfDatesForUser(long userId){
        getAllUserStocks();
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        stocks.forEach(stock -> {
            if(stock.getUserId() == userId) {
                if (!timestamps.contains(stock.getCreatedOn())) {
                    timestamps.add(stock.getCreatedOn());
                }
            }
        });

        ArrayList<Date> dates = new ArrayList<>();
        timestamps.forEach(timestamp -> {
            dates.add(new Date(timestamp.getTime()));
        });
        return dates;
    }

    public ArrayList<Date> getListOfDatesInTable(){
        getAllUserStocks();
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        stocks.forEach(stock -> {
            if(!timestamps.contains(stock.getCreatedOn())){
                timestamps.add(stock.getCreatedOn());
            }
        });

        ArrayList<Date> dates = new ArrayList<>();
        timestamps.forEach(timestamp -> {
            dates.add(new Date(timestamp.getTime()));
        });
        return dates;
    }

    public ArrayList<UserStock> getAllStocksOfUser(long userId, long accountNumber){
        ArrayList<UserStock> stocksOfUser = new ArrayList<>();

        UserStocks userStocksClass = getUserStocks(userId);
        if (userStocksClass == null) {
            return stocksOfUser;
        }

        userStocksClass.getUserStocks().forEach((integer, stock) -> {
            if(stock.getAccountNumber()==accountNumber) {
                stocksOfUser.add(stock);
            }
        });

        return stocksOfUser;
    }
}
