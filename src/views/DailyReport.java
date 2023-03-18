package src.views;

import src.Database.Models.Account;
import src.Database.Models.Transaction;
import src.Database.Models.UserStock;
import src.manager.Manager;
import src.views.formatter.DateView;
import src.views.formatter.StockView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the DailyReport class, which shows a report of transactions, accounts, and stocks for a specific day.
 * It contains a drop-down menu to select the day to view, tables to display transaction, account, and stock data,
 * as well as labels to show the number of transactions, accounts, and stocks.
 * The class also has methods to initialize and update the tables and drop-down menu.
 */
public class DailyReport extends PageBack {
    private JButton backButton;
    private JComboBox selectionComboBox;
    private JLabel queryLabel;
    private JTable transactionTable;
    private JPanel rootPanel;
    private JTable accountTable;
    private JTable stockTable;
    private JLabel transactionCountLabel;
    private JLabel accountCountLabel;
    private JLabel stockCountLabel;
    private JLabel errorLabel;
    private Manager manager;
    private ArrayList<Date> dates;

    public DailyReport() {
        setManager(new Manager());
        initDateFilter();
        initTransactionTable();
        initStockTable();
        initAccountTable();

        updateTransactions(dates.get(0));
        updateStocks(dates.get(0));
        updateAccountActivities(dates.get(0));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                headBack();
            }
        });

        selectionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionComboBox.getSelectedIndex() < 0) {
                    return;
                }
                Date today = dates.get(selectionComboBox.getSelectedIndex());
                updateTransactions(today);
                updateStocks(today);
                updateAccountActivities(today);
            }
        });
    }

    private void initTransactionTable() {
        //public class Transaction {
        //    String txnId;
        //    double txnAmount;
        //    String txnType;
        //    Timestamp txnDate;
        //    String txnNarration;
        //    long accountNumber;
        //    long userId;
        //}
        String[] column = {"Time", "User ID", "Account Number", "Transaction Type", "Amount"};
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        for (String col : column) {
            model.addColumn(col);
        }
    }

    private void updateTransactions(Date date) {
        System.out.println(date);
        transactionCountLabel.setText(String.valueOf(manager.getDailyTransactionsCount(date)));
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0); // Delete all raws.
        for (Transaction transaction : manager.getDailyTransactions(date)) {
            model.addRow(new Object[]{
                    transaction.getTxnDate(),
                    transaction.getUserId(),
                    transaction.getAccountNumber(),
                    transaction.getTxnType(),
                    transaction.getTxnAmount(),
            });
        }
    }

    private void initDateFilter() {
        Set<String> targetSet = new HashSet<String>();
        dates = (new Manager()).getListOfDatesForFilter();
        if (dates.size() == 0) {
            errorLabel.setText("* No record to display.");
            return;
        }
        for (Date date : dates) {
            targetSet.add(DateView.formatDate(date));
        }
        for (String s : targetSet) {
            selectionComboBox.addItem(s);
        }
    }

    private void initStockTable() {
        // public class UserStock {
        //    int id;
        //    long userId;
        //    long accountNumber;
        //    int stockPurchased;
        //    int stockSold;
        //    double buyPrice;
        //    double sellPrice;
        //
        //    int stockId;
        //    String stockName;
        //    Timestamp createdOn;
        //    Timestamp updatedOn;
        // }
        String[] column = {"Time", "User ID", "Account Number", "Stock Name", "Purchase", "Sell"};
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        for (String col : column) {
            model.addColumn(col);
        }
    }

    private void updateStocks(Date date) {
        stockCountLabel.setText(String.valueOf(manager.getDailyStocksCount(date)));
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.setRowCount(0); // Delete all raws.
        for (UserStock stock : manager.getDailyStocks(date)) {
            model.addRow(new Object[]{
                    stock.getUpdatedOn(),
                    stock.getUserId(),
                    stock.getAccountNumber(),
                    stock.getStockName(),
                    StockView.formatStockTrade(stock.getStockPurchased(), stock.getBuyPrice()),
                    StockView.formatStockTrade(stock.getStockSold(), stock.getSellPrice()),
            });
        }
    }

    private void initAccountTable() {
        //public class Account {
        //    private long accountNumber;
        //    private double balance;
        //    private long userId;
        //    private boolean accountLocked; // Is account locked?
        //    private boolean accountDeleted; // Is account deleted?
        //    private String type;
        //
        //    String accountCurrency;
        //
        //    private double transactionLimit; // How much money can be withdrawn
        //    private double interestRate;
        //    private double interestGenerated; //Total interest generated on this account
        //    private boolean debitCard; // does user have a debit card or not?
        //    private boolean creditCard; // does user have a credit card or not?
        //    private ArrayList<Transaction> transactions;
        //
        //    private Timestamp createdOn; // Date account was creaated on
        //    private Timestamp deletedOn; // Date account was deleted on
        // }
        String[] column = {"Time", "User ID", "Account Number", "Currency", "Balance"};
        DefaultTableModel model = (DefaultTableModel) accountTable.getModel();
        for (String col : column) {
            model.addColumn(col);
        }
    }

    private void updateAccountActivities(Date date) {
        accountCountLabel.setText(String.valueOf(manager.getDailyAccountsCount(date)));
        DefaultTableModel model = (DefaultTableModel) accountTable.getModel();
        model.setRowCount(0); // Delete all raws.
        for (Account account : manager.getDailyAccounts(date)) {
            model.addRow(new Object[]{
                    account.getCreatedOn(),
                    account.getUserId(),
                    account.getAccountNumber(),
                    account.getAccountCurrency(),
                    account.getBalance(),
            });
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 4, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectionComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        selectionComboBox.setModel(defaultComboBoxModel1);
        rootPanel.add(selectionComboBox, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        rootPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        queryLabel = new JLabel();
        queryLabel.setText("Which day do you want to view?");
        rootPanel.add(queryLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        rootPanel.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        tabbedPane1.addTab("Transactions", scrollPane1);
        transactionTable = new JTable();
        transactionTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
        scrollPane1.setViewportView(transactionTable);
        final JScrollPane scrollPane2 = new JScrollPane();
        tabbedPane1.addTab("Stocks", scrollPane2);
        stockTable = new JTable();
        stockTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
        scrollPane2.setViewportView(stockTable);
        final JScrollPane scrollPane3 = new JScrollPane();
        tabbedPane1.addTab("Accounts Creation", scrollPane3);
        accountTable = new JTable();
        accountTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
        scrollPane3.setViewportView(accountTable);
        final JLabel label1 = new JLabel();
        label1.setText("Transactions done: ");
        rootPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Stocks trades:");
        rootPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transactionCountLabel = new JLabel();
        transactionCountLabel.setText("<count>");
        rootPanel.add(transactionCountLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stockCountLabel = new JLabel();
        stockCountLabel.setText("<count>");
        rootPanel.add(stockCountLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Accounts created:");
        rootPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accountCountLabel = new JLabel();
        accountCountLabel.setText("<count>");
        rootPanel.add(accountCountLabel, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-65536));
        errorLabel.setText("");
        rootPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
