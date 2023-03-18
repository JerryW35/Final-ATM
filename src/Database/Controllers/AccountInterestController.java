package src.Database.Controllers;

import src.Database.Models.AccountInterest;
import src.Helpers.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class AccountInterestController {
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<String, AccountInterest> interests = new HashMap<>();

    public void createNewInterestRate(String accountType, double interestRate){
        Connection connection = sqlConnection.getConnection();

        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO `account_interest`(account_type, interest_rate) VALUES (?,?)");
            pstmt.setString(1, accountType);
            pstmt.setDouble(2, interestRate);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void updateInterestRate(String accountType, double interestRate){
        System.out.println(accountType + " " + interestRate);
        Connection connection = sqlConnection.getConnection();

        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE `account_interest` SET interest_rate=? WHERE account_type=?");
            pstmt.setDouble(1, interestRate);
            pstmt.setString(2, accountType);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public ResultSet getAllInterestRates() {
        Connection connection = sqlConnection.getConnection();

        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("SELECT * from `account_interest`");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populateInterests(){
        ResultSet rs = getAllInterestRates();

        // Condition check
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                AccountInterest accountInterest = new AccountInterest();
                accountInterest.setInterestRate(rs.getDouble("interest_rate"));
                accountInterest.setAccountType(rs.getString("account_type"));
                accountInterest.setId(rs.getInt("id"));
                interests.put(accountInterest.getAccountType(), accountInterest);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public double returnAccountInterestRate(String accountType){
        populateInterests();
        return interests.get(accountType).getInterestRate();
    }
}
