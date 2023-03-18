package src.Database.Models;

import java.sql.Timestamp;

/*
Following the MVC structure this is the design for the Transaction model
 */

public class Transaction {
    String txnId;
    double txnAmount;
    String txnType;
    Timestamp txnDate;
    String txnNarration;
    long accountNumber;
    long userId;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public double getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(double txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public Timestamp getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Timestamp txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnNarration() {
        return txnNarration;
    }

    public void setTxnNarration(String txnNarration) {
        this.txnNarration = txnNarration;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
