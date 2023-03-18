package src.transaction;

import java.util.ArrayList;

/*
Transaction Factory implements the factory pattern for transactions
Based on the input provided by the user for which account they are performing the transaction on,
the factory returns the specific transaction to process the transfer. Different types of transactions are as follows:
    - Account Transaction
        This allows for between account transfers to helps us make internal transfers
    - Fee Transactions
        This is a transaction generated for every fee that is present so as to allow for a transaction to take place
    - Interest Transaction
    - Loan Transaction
    - Stock transaction
 */
public class TransactionViewBehavior extends TransactionFactory {
    public ArrayList<TransactionFactory> getTransactions(ArrayList<TransactionFactory> transactions) {
        return transactions;
    }
}
