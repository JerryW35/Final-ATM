package src.Database.Controllers;

import src.Database.Models.Currency;
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

public class CurrencyController {
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<String, Currency> conversionRates = new HashMap<>();

    public double getAmountInUSD(double amount, String currencySymbol){
        getAllRates();
        return amount * conversionRates.get(currencySymbol).getConversionRate();
    }

    public double getAmountInCurrency(double amount, String currencySymbol){
        getAllRates();
        return amount / conversionRates.get(currencySymbol).getConversionRate();
    }

    public HashMap<String, String> getAllCurrencies() {
        getAllRates();
        HashMap<String, String> currencies = new HashMap<>();
        conversionRates.forEach((s, currency) -> {
            currencies.put(s, currency.getCurrencyName());
        });
        return currencies;
    }

    private void getAllRates(){
        conversionRates.clear();
        String sql = "select * from currency";
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
            try {
                Currency currency = new Currency();
                currency.setCurrencyName(rs.getString("currency_name"));
                currency.setId(rs.getInt("id"));
                currency.setCurrencySymbol(rs.getString("currency_symbol"));
                currency.setConversionRate(rs.getDouble("conversion_rate"));
                conversionRates.put(currency.getCurrencySymbol(), currency);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, Double> getAllCurrencyRates(){
        getAllRates();
        HashMap<String, Double> currencyRates = new HashMap<>();
        conversionRates.forEach((s, currency) -> {
            currencyRates.put(s, currency.getConversionRate());
        });
        return currencyRates;
    }
}
