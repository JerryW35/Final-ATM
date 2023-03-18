package src.ATM;

import org.apache.commons.beanutils.BeanUtils;
import src.Database.Controllers.AccountController;
import src.Database.Controllers.CurrencyController;
import src.account.AccountFactory;
import src.account.AccountImplementation;

import java.lang.reflect.InvocationTargetException;

/*
This class specifically belongs for the ATM functionalities of the project

It allows depositMoney and has a single functionality in accordance with SOLID Principles
 */

public class Deposit {
    public double depositMoney(long accountNumber, double amount, String amountCurrency){
        AccountController accountController = new AccountController();
        CurrencyController currencyController = new CurrencyController();
        AccountFactory accountFactory = new AccountFactory();
        AccountImplementation account = accountFactory.getByTypeOfAccount(accountController.getAccountByNumber(accountNumber).getType());

        try {
            BeanUtils.copyProperties(account, accountController.getAccountByNumber(accountNumber));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        String currency = account.getAccountCurrency();
        double amountInUSD = currencyController.getAmountInUSD(amount, amountCurrency.substring(0,3));
        account.depositMoney(amountInUSD);
        return account.getBalance();
    }
}
