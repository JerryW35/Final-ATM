package src.Database.Controllers;

import src.Database.Models.Account;
import src.Helpers.DateConverter;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class AccountController {
    //    public boolean createNewAccount()
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<Long, Account> accounts = new HashMap<>();

    HashMap<Long, ArrayList<Account>> accountsByUser = new HashMap<>();

    public boolean createNewAccount(String account_type, double starting_balance, long user_id, double transaction_limit, double interest_rate, boolean debit_card_issued, boolean credit_card_issued, String accountCurrency) {
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Timestamp updateAt = createdAt;
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `account`(account_type, balance, deleted, blocked, user_id, transaction_limit, interest_rate, debit_card_issued, credit_card_issued, created_at, updated_at, account_currency) VALUE (?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, account_type);
            pstmt.setDouble(2, starting_balance);
            pstmt.setBoolean(3, false);
            pstmt.setBoolean(4, false);
            pstmt.setLong(5, user_id);
            pstmt.setDouble(6, transaction_limit);
            pstmt.setDouble(7, interest_rate);
            pstmt.setBoolean(8, debit_card_issued);
            pstmt.setBoolean(9, credit_card_issued);
            Timestamp created_at = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(10, created_at);
            pstmt.setTimestamp(11, created_at);
            pstmt.setString(12, accountCurrency);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void updateAccountBalance(long accountNumber, double newBalance){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
            PreparedStatement pstmt = connection.prepareStatement("UPDATE `account` SET balance=?, updated_at=? WHERE account_no=?");
            pstmt.setDouble(1, newBalance);
            pstmt.setLong(3, accountNumber);
            pstmt.setTimestamp(2, updatedAt);
            int returnValue = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getAllAccounts() {
        accounts.clear();

        String sql = "select * from account WHERE deleted=?";
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setBoolean(1, false);
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

            Account account = new Account();

            try {
                account.setAccountNumber(rs.getLong("account_no"));
                account.setType(rs.getString("account_type"));
                CurrencyController currencyController = new CurrencyController();
                account.setBalance(rs.getDouble("balance"));
                account.setCreatedOn(rs.getTimestamp("created_at"));
                account.setDeletedOn(rs.getTimestamp("updated_at"));
                account.setAccountDeleted(rs.getBoolean("deleted"));
                account.setAccountLocked(rs.getBoolean("blocked"));
                account.setUserId(rs.getLong("user_id"));
                account.setTransactionLimit(rs.getDouble("transaction_limit"));
                account.setInterestRate(rs.getDouble("interest_rate"));
                account.setInterestGenerated(rs.getDouble("interest_generated"));
                account.setDebitCard(rs.getBoolean("debit_card_issued"));
                account.setCreditCard(rs.getBoolean("credit_card_issued"));
                account.setAccountCurrency(rs.getString("account_currency"));

                account.setBalance(currencyController.getAmountInCurrency(account.getBalance(), account.getAccountCurrency()));

                TransactionController transactionController = new TransactionController();
                account.setTransactions(transactionController.getTransactionsByAccountNumber(account.getAccountNumber()));

                accounts.put(account.getAccountNumber(), account);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public boolean deleteAccount(long accountNumber){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp updateAt = new Timestamp(System.currentTimeMillis());
            PreparedStatement pstmt = connection.prepareStatement("UPDATE account SET deleted=? WHERE account_no=?");
            pstmt.setBoolean(1, true);
            pstmt.setLong(2, accountNumber);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public ArrayList<Account> getAllAccountsOfUser(long userId) {
        getAllAccounts();
        getAllAccountsByUsers();
        ArrayList<Account> accounts1 = new ArrayList<>();
        accounts1 = accountsByUser.get(userId);
        return accounts1;
    }

    public HashMap<Long, Account> getAccounts() {
        getAllAccounts();
        return accounts;
    }

    public void setAccounts(HashMap<Long, Account> accounts) {
        this.accounts = accounts;
    }

    public boolean checkIfAccountsExist(long userId) {
        UserController userController = new UserController();
        userController.getAllUsers();
        if (accounts.containsKey(userId) && userController.checkIfUserExists(userId)) {
            return true;
        } else {
            return false;
        }
    }

    public HashMap<Long, ArrayList<Account>> getAllAccountsByUsers() {
        accountsByUser.clear();
        getAccounts();

        accounts.forEach((account_number, account) -> {
            if (accountsByUser.containsKey(account.getUserId())) {
                accountsByUser.get(account.getUserId()).add(account);
            } else {
                ArrayList<Account> accounts1 = new ArrayList<>();
                accounts1.add(account);
                accountsByUser.put(account.getUserId(), accounts1);
            }
        });

        return getAccountsByUser();
    }

    public HashMap<Long, ArrayList<Account>> getAccountsByUser() {
        return accountsByUser;
    }

    public void setAccountsByUser(HashMap<Long, ArrayList<Account>> accountsByUser) {
        this.accountsByUser = accountsByUser;
    }

    public Account getAccountByNumber(long accountNumber){
        getAllAccounts();
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAllAccountsBetweenDate(Date start, Date end){
        getAllAccounts();
        ArrayList<Account> accountsArray = getAccountsArray();
        return accountsArray.stream().filter(account -> account.getCreatedOn().after(new Timestamp(start.getTime())) && account.getCreatedOn().before(new Timestamp(end.getTime())))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public ArrayList<Account> getAllAccountsOnDate(Date currentDate){
        getAllAccounts();
        ArrayList<Account> accountsArray = getAccountsArray();
        return accountsArray.stream().filter(account -> account.getCreatedOn().after(new Timestamp(currentDate.getTime())) && account.getCreatedOn().before(new Timestamp(currentDate.getTime() + DateConverter.MILLIS_IN_A_DAY)))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public int getAllAccountsOnDateCount(Date currentDate){
        return getAllAccountsOnDate(currentDate).size();
    }

    private ArrayList<Account> getAccountsArray(){
        ArrayList<Account> accountsArray = new ArrayList<>();
        accounts.forEach((aLong, account) -> {
            accountsArray.add(account);
        });
        return accountsArray;
    }

    public double getAccountBalance(long accountNumber){
        Account account = getAccountByNumber(accountNumber);
        return account.getBalance();
    }

    public void addInterestToAccounts(Date date){
        getAllAccountsByUsers().forEach((aLong, accounts1) -> {
            accounts1.forEach(account -> {
                if(account.getInterestRate()>0 && account.getType().equalsIgnoreCase("savings")){
                    double change = account.getBalance()*account.getInterestRate()/100;
                    double newBalance = account.getBalance() + change;
                    updateAccountBalance(account.getAccountNumber(), newBalance);
                    TransactionController transactionController = new TransactionController();
                    transactionController.createNewTransaction(change, "Credit", "Interest for account on"+date.toString(), account.getAccountNumber(), account.getUserId());
                }
            });
        });
    }

    public double getAccountInterestRate(long accountNumber){
        Account account = getAccountByNumber(accountNumber);
        return account.getInterestRate();
    }

    public void getInterestFromLoanAccounts(Date date){
        getAllAccountsByUsers().forEach((aLong, accounts1) -> {
            accounts1.forEach(account -> {
                if(account.getType().equalsIgnoreCase("loan")){
                    LoanController loanController = new LoanController();
                    double loanAmount = loanController.getLoanAmount(account.getAccountNumber(), account.getUserId());
                    double change = loanAmount*account.getInterestRate()/100;
                    double newBalance = account.getBalance() - change;
                    updateAccountBalance(account.getAccountNumber(), newBalance);
                    TransactionController transactionController = new TransactionController();
                    transactionController.createNewTransaction(change, "Debit", "Interest for loan on"+date.toString(), account.getAccountNumber(), account.getUserId());
                }
            });
        });
    }

    public ArrayList<Date> getListOfDatesForUser(long userId){
        ArrayList<Account> accountsOfUser = getAllAccountsOfUser(userId);
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        accountsOfUser.forEach(account -> {
            if(!timestamps.contains(account.getCreatedOn())){
                timestamps.add(account.getCreatedOn());
            }
        });

        ArrayList<Date> dates = new ArrayList<>();
        timestamps.forEach(timestamp -> {
            dates.add(new Date(timestamp.getTime()));
        });
        return dates;
    }

    public ArrayList<Date> getListOfDatesInTable(){
        HashMap<Long, Account> accountHashMap = getAccounts();
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        accountHashMap.forEach((l, account) -> {
            if(!timestamps.contains(account.getCreatedOn())){
                timestamps.add(account.getCreatedOn());
            }
        });

        ArrayList<Date> dates = new ArrayList<>();
        timestamps.forEach(timestamp -> {
            dates.add(new Date(timestamp.getTime()));
        });
        return dates;
    }


    public boolean checkIfInterestUnpaidOnLoan(long userId){
        AtomicBoolean status = new AtomicBoolean(false);
        getAllAccountsOfUser(userId).forEach(account -> {
            if(account.getType().equalsIgnoreCase("loan")){
                if(account.getBalance()<0){
                    status.set(true);
                }
            }
        });

        return status.get();
    }

    public boolean checkIfAccountExists(long accountNumber){
        getAllAccounts();
        return accounts.containsKey(accountNumber);
    }

}
