package src.views.formatter;

import src.Database.Controllers.CurrencyController;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyView {
    private static final HashMap<String, String> currencies = (new CurrencyController()).getAllCurrencies();
    public static String[] getCurrencyList() {
        ArrayList<String> items = new ArrayList<>();
        for( String s: currencies.keySet()) {
            items.add(formatCurrency(s));
        }
        // Reference: https://stackoverflow.com/questions/4042434/converting-arrayliststring-to-string-in-java
        return items.toArray(new String[0]);
    }

    public static String formatCurrency(String abbreviate) {
        return abbreviate + " (" + currencies.get(abbreviate) + ")";
    }
}
