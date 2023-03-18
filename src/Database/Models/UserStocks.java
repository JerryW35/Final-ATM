package src.Database.Models;

import java.util.HashMap;


/*
Following the MVC structure this is the design for the User Stocks Model. Different from user Stock as this contains all the stocks that a user has purchased
in arraylist format
 */
public class UserStocks {
    long userId;
    HashMap<Integer, UserStock> userStocks = new HashMap<>();

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public HashMap<Integer, UserStock> getUserStocks() {
        return userStocks;
    }

    public void setUserStocks(HashMap<Integer, UserStock> userStocks) {
        this.userStocks = userStocks;
    }
}
