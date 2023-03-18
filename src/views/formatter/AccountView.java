package src.views.formatter;

import src.Database.Models.Account;

public class AccountView {
    public static String getType(Account a) {
        return "[" + a.getType() + "]";
    }

    public static String getNumber(Account a) {
        return "# " + a.getAccountNumber();
    }

    public static String getBalance(Account a) {
        return a.getAccountCurrency() + " $ " + a.getBalance();
    }
}
