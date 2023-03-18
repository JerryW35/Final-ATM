package src.Database.Controllers;

import src.Database.Models.LoanModel;
import src.Helpers.DateConverter;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class LoanController {
    SQLConnection sqlConnection = new SQLConnection();
    ArrayList<LoanModel> loans = new ArrayList<>();
    public boolean createNewLoan(LoanModel loan) {
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        if(checkIfLoanExists(loan.getAccountNumber(), loan.getUserId())){
            try {
                Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                Timestamp updateAt = createdAt;
                PreparedStatement pstmt = connection.prepareStatement("UPDATE `loan` SET loan_amount=? WHERE account_no=? AND user_id=?");
                pstmt.setDouble(1, getLoanAmount(loan.getAccountNumber(), loan.getUserId()) + loan.getLoanAmount());
                pstmt.setLong(2, loan.getAccountNumber());
                pstmt.setLong(3, loan.getUserId());
                int returnValue = pstmt.executeUpdate();

                if (returnValue > 0) {
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                Timestamp updateAt = createdAt;
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `loan`(user_id, account_no, due_date, created_at, interest_amount, loan_amount, loan_purpose) VALUE (?,?,?,?,?,?,?)");
                pstmt.setLong(1, loan.getUserId());
                pstmt.setLong(2, loan.getAccountNumber());
                pstmt.setTimestamp(3, loan.getDueDate());
                pstmt.setTimestamp(4, loan.getCreatedAt());
                pstmt.setDouble(5, loan.getInterestAmount());
                pstmt.setDouble(6, loan.getLoanAmount());
                pstmt.setString(7, loan.getLoanPurpose());
                int returnValue = pstmt.executeUpdate();

                if (returnValue > 0) {
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }


    public void getAllLoans(){
        loans.clear();

        String sql = "select * from loan";
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
                LoanModel loanModel = new LoanModel();
                loanModel.setLoanPurpose(rs.getString("loan_purpose"));
                loanModel.setLoanAmount(rs.getDouble("loan_amount"));
                loanModel.setAccountNumber(rs.getLong("account_no"));
                loanModel.setUserId(rs.getLong("user_id"));
                loanModel.setId(rs.getInt("id"));
                loanModel.setDueDate(rs.getTimestamp("due_date"));
                loanModel.setCreatedAt(rs.getTimestamp("created_at"));
                loanModel.setInterestAmount(rs.getDouble("interest_amount"));
                loans.add(loanModel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<LoanModel> getAllLoansOnDate(Date currentDate){
        getAllLoans();
        Long time = currentDate.getTime();
        time = time - time % (24 * 60 * 60 * 1000);
        Long finalTime = time;
        return loans.stream().filter(loan -> loan.getCreatedAt().after(new Timestamp(finalTime)) && loan.getCreatedAt().before(new Timestamp(finalTime + DateConverter.MILLIS_IN_A_DAY)))
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public int getAllLoansOnDateCount(Date currentDate){
        return getAllLoansOnDate(currentDate).size();
    }

    public boolean checkIfLoanExists(long accountNumber, long userId){

        AtomicBoolean status= new AtomicBoolean(false);
        getAllLoans();
        loans.forEach(loanModel -> {
            if(loanModel.getAccountNumber()==accountNumber && loanModel.getUserId()==userId){
                status.set(true);
            }
        });

        return status.get();
    }

    public double getLoanAmount(long accountNumber, long userId){
        AtomicReference<Double> loanAmount = new AtomicReference<>((double) 0);
        getAllLoans();
        if(checkIfLoanExists(accountNumber, userId)){
            loans.forEach(loanModel -> {
                if (loanModel.getAccountNumber() == accountNumber && loanModel.getUserId() == userId) {
                    loanAmount.set(loanModel.getLoanAmount());
                }
            });
        }
        return loanAmount.get();
    }
    
}
