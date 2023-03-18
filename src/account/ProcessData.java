package src.account;

import org.apache.commons.beanutils.BeanUtils;
import src.Database.Controllers.AccountController;
import src.Database.Controllers.BeneficiaryController;
import src.Database.Models.Account;
import src.Database.Models.Beneficiary;
import src.user.LoggedInUser;
import src.user.UserView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/*
This class is used to actually process data that is recieved from Users. This class is more like a middleman between the factory and the UI
The UI sends data here, which is converted, validated, checked for any errors and then passed to the factory for processing and further function creations
 */

public class ProcessData {
    public boolean process(UserView user, String accountType, Account accountData){
        // process the info from user and create the account

        AccountFactory facotory = new AccountFactory();
        AccountController accountController = new AccountController();
        HashMap<Long, Account> allAccounts = new HashMap<>();
        if(accountController.getAllAccountsOfUser(LoggedInUser.getUser().getUserId()) != null) {
            accountController.getAllAccountsOfUser(LoggedInUser.getUser().getUserId()).forEach(account -> {
                Account accountImplementation = new Account();
                try {
                    BeanUtils.copyProperties(accountImplementation, account);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                allAccounts.put(account.getAccountNumber(), accountImplementation);
            });
        }
        // check accountData.balance >0
        // check if account type securities then get user accounts and check savings account balance > 5000
        // if account type securities then accountData.balance > 1000
        // if account type loan then get user accounts and check length of accounts > 0

        if(accountData.getBalance()>0) {
            if (accountData.getType().equalsIgnoreCase("securities") && checkBalance(user, 5000) && accountData.getBalance() > 1000) {
                facotory.createAccount(accountData);
            }else if (accountData.getType().equalsIgnoreCase("loan") && allAccounts.size() > 0) {
                facotory.createAccount(accountData);
            }else if(accountData.getType().equalsIgnoreCase("savings") || accountData.getType().equalsIgnoreCase("checking")){
                //for checking and saving accounts
                facotory.createAccount(accountData);
            }else{
                return false;
            }
            return true;
        }
        return false;
    }

    private static boolean checkBalance(UserView user, int amount){
        AtomicReference<Double> totalBalance = new AtomicReference<>((double) 0);
        AccountController accountController = new AccountController();
        ArrayList<Account> userAccounts = accountController.getAllAccountsOfUser(user.getUserId());
        if(userAccounts.size()>0){
            userAccounts.forEach(account -> {
                totalBalance.updateAndGet(v -> new Double((double) (v + account.getBalance())));
            });
        }

        return totalBalance.get()>amount;
//        double totalBalance=0;
//        for(AccountImplementation account: user.getAccounts().values()){
//            if(account.getAccountType().equalsIgnoreCase("savings")&&account.getBalance()>0){
//                totalBalance+=account.getBalance();
//            }
//        }
//        return totalBalance>amount;
    }

    public HashMap<String, Long> getAccountsOfUser(long userId){
        HashMap<String, Long> fromAccounts = new HashMap<>(); //This will be used to populate dropdown of from accounts during transactions
        AccountController accountController = new AccountController();
        ArrayList<Account> accounts = accountController.getAllAccountsOfUser(userId);
        accounts.forEach(account -> {
            fromAccounts.put((account.getType()+"-"+account.getAccountCurrency()), account.getAccountNumber());
        });
        return fromAccounts;
    }

    public HashMap<String, Long> getAccountsToTransferTo(long userId, long fromAccount){
        //This function will give a hashmap of accounts the user can transfer money to (beneficiaries and their own accounts)
        HashMap<String, Long> toAccounts = new HashMap<>();
        AccountController accountController = new AccountController();
        ArrayList<Account> accounts = accountController.getAllAccountsOfUser(userId);
        accounts.forEach(account -> {
            if(account.getAccountNumber()!=fromAccount) {
                toAccounts.put((account.getType() + "-" + account.getAccountCurrency()), account.getAccountNumber());
            }
        });
        BeneficiaryController beneficiaryController = new BeneficiaryController();
        ArrayList<Beneficiary> beneficiaries = beneficiaryController.getAllBeneficiariesOfUser(userId);
        if (beneficiaries!=null) {
            beneficiaries.forEach(beneficiary -> {
                toAccounts.put(beneficiary.getBenName() + "-" + beneficiary.getBenRoutingNo(), beneficiary.getBenAccNo());
            });
        }

        return toAccounts;
    }
}
