package src.Database.Controllers;

import src.Database.Models.Transaction;
import src.Helpers.DateConverter;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class TransactionController {

    SQLConnection sqlConnection = new SQLConnection();

    HashMap<Long, ArrayList<Transaction>> transactionsByAccount = new HashMap<>();

    ArrayList<Transaction> transactions = new ArrayList<>();
    public boolean createNewTransaction(double txn_amount, String txn_type, String txn_narration, long accountNumber, long userId) {
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `transaction`(id, amount, txn_type, txn_date, txn_narration, account_no, user_id) VALUE (?,?,?,?,?,?,?)");
            UUID uuid = UUID.randomUUID();
            pstmt.setString(1, uuid.toString());
            pstmt.setDouble(2, txn_amount);
            pstmt.setString(3, txn_type);
            pstmt.setTimestamp(4, createdAt);
            pstmt.setString(5, txn_narration);
            pstmt.setLong(6, accountNumber);
            pstmt.setLong(7, userId);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public HashMap<Long, ArrayList<Transaction>> getTransactionsByAccount() {
        getAllTransactionsByAccount();
        return transactionsByAccount;
    }

    public void getAllTransactionsByAccount(){
        transactionsByAccount.clear();
        String sql = "select * from transaction";
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
                long accountNumber = rs.getLong("account_no");
                if(transactionsByAccount.containsKey(accountNumber)){
                    ArrayList<Transaction> transactionsExisting = transactionsByAccount.get(accountNumber);
                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(accountNumber);
                    transaction.setTxnAmount(rs.getDouble("amount"));
                    transaction.setUserId(rs.getLong("user_id"));
                    transaction.setTxnId(rs.getString("id"));
                    transaction.setTxnDate(rs.getTimestamp("txn_date"));
                    transaction.setTxnType(rs.getString("txn_type"));
                    transaction.setTxnNarration(rs.getString("txn_narration"));
                    transactionsExisting.add(transaction);
                }else{
                    ArrayList<Transaction> transactionsExisting = new ArrayList<>();
                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(accountNumber);
                    transaction.setTxnAmount(rs.getDouble("amount"));
                    transaction.setUserId(rs.getLong("user_id"));
                    transaction.setTxnId(rs.getString("id"));
                    transaction.setTxnDate(rs.getTimestamp("txn_date"));
                    transaction.setTxnType(rs.getString("txn_type"));
                    transaction.setTxnNarration(rs.getString("txn_narration"));
                    transactionsExisting.add(transaction);
                    transactionsByAccount.put(accountNumber, transactionsExisting);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void getAllTransactions(){
        transactions.clear();

        String sql = "select * from transaction";
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
                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(rs.getLong("account_no"));
                    transaction.setTxnAmount(rs.getDouble("amount"));
                    transaction.setUserId(rs.getLong("user_id"));
                    transaction.setTxnId(rs.getString("id"));
                    transaction.setTxnDate(rs.getTimestamp("txn_date"));
                    transaction.setTxnType(rs.getString("txn_type"));
                    transaction.setTxnNarration(rs.getString("txn_narration"));
                    transactions.add(transaction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Transaction> getTransactionsByAccountNumber(long accountNumber){
        getAllTransactionsByAccount();
        return transactionsByAccount.get(accountNumber);
    }

    public ArrayList<Transaction> getAllTransactionsBetweenDate(Date start, Date end){
        getAllTransactions();
        return transactions.stream().filter(transaction -> transaction.getTxnDate().after(new Timestamp(start.getTime())) && transaction.getTxnDate().before(new Timestamp(end.getTime())))
                .collect(Collectors
                .toCollection(ArrayList::new));
    }

    public ArrayList<Transaction> getAllTransactionsOnDate(Date currentDate){
        getAllTransactions();
        Long time = currentDate.getTime();
        time = time - time % (24 * 60 * 60 * 1000);
        Long finalTime = time;
        return transactions.stream().filter(transaction -> transaction.getTxnDate().after(new Timestamp(finalTime)) && transaction.getTxnDate().before(new Timestamp(finalTime + DateConverter.MILLIS_IN_A_DAY)))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public int getAllTransactionsOnDateCount(Date currentDate){
        return getAllTransactionsOnDate(currentDate).size();
    }

    public ArrayList<Transaction> getAllTransactionsByEndDate(Date end){
        getAllTransactions();
        return transactions.stream().filter(transaction -> transaction.getTxnDate().before(new Timestamp(end.getTime())))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public ArrayList<Transaction> getAllTransactionsByStartDate(Date start){
        getAllTransactions();
        return transactions.stream().filter(transaction -> transaction.getTxnDate().after(new Timestamp(start.getTime())))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public ArrayList<Date> getListOfDatesForUser(long userId){
        getAllTransactions();
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        transactions.forEach(transaction -> {
            if(transaction.getUserId() == userId) {
                if (!timestamps.contains(transaction.getTxnDate())) {
                    timestamps.add(transaction.getTxnDate());
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
        getAllTransactions();
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        transactions.forEach(transaction -> {
            if(!timestamps.contains(transaction.getTxnDate())){
                timestamps.add(transaction.getTxnDate());
            }
        });

        ArrayList<Date> dates = new ArrayList<>();
        timestamps.forEach(timestamp -> {
            dates.add(new Date(timestamp.getTime()));
        });
        return dates;
    }
}
