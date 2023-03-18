package src.views;

import src.Database.Controllers.AccountController;
import src.Database.Models.Account;
import src.user.LoggedInUser;
import src.user.UserView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This is the UserLanding class, which provides a user interface for logged in users to manage their accounts and view account information.
 * It contains methods for logging out, creating new accounts, viewing beneficiaries, and viewing currency information.
 * The class uses the AccountController to interact with the database to retrieve and update account information.
 */
public class UserLanding extends PageBack {
    private static final String[] column = {"Account Number", "Type", "Currency", "Balance"};
    private JButton btnCreateAccount;
    private JButton logoutButton;
    private JPanel rootPanel;
    private JLabel greetingLabel;
    private JTable accountTable;
    private JButton addANewReceiverButton;
    private JButton viewCurrentCurrencyButton;
    private JButton viewNotification;
    private final UserView user;
    private final AccountController accountController = new AccountController();
    private ArrayList<Account> accounts;

    // views for buttons to go.
    private final BeneficiaryCreation bCreation = new BeneficiaryCreation();
    private final AccountCreation accountCreation = new AccountCreation();
    private final CurrencyTable currencyTable = new CurrencyTable();

    public UserLanding(UserView user) {
        this.user = user;
        this.initTable();
        this.updateUserInfo();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainView.removeAllPanel();
                MainView.setContentPanel((new UserLogin()).getRootPanel());
            }
        });

        btnCreateAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountCreation.setUser(user);
                accountCreation.setPreviousPanel(rootPanel);
                MainView.setContentPanel(accountCreation.getPanel());
            }
        });

        accountTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow < 0 || selectedRow >= accounts.size()) {
                    return;
                }
                String accountNumber = accountTable.getValueAt(selectedRow, 0).toString();
                Account a = accountController.getAccountByNumber(Long.parseLong(accountNumber));
                AccountPage accountPage = new AccountPage(a);
                accountPage.setUser(user);
                accountPage.setPreviousPanel(rootPanel);
                MainView.setContentPanel(accountPage.getPanel());
            }
        });

        addANewReceiverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bCreation.setUserId(user.getUserId());
                bCreation.setPreviousPanel(rootPanel);
                MainView.setContentPanel(bCreation.getRootPanel());
            }
        });

        viewCurrentCurrencyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currencyTable.setPreviousPanel(rootPanel);
                MainView.setContentPanel(currencyTable.getRootPanel());
            }
        });

        viewNotification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotificationTable notificationTable = new NotificationTable(LoggedInUser.getUser().getUserId());
                notificationTable.setPreviousPanel(rootPanel);
                MainView.setContentPanel(notificationTable.getRootPanel());
            }
        });

        rootPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateUserInfo();
                super.componentShown(e);
            }
        });
    }

    public void initTable() {
        DefaultTableModel model = (DefaultTableModel) accountTable.getModel();
        for (String col : column) {
            model.addColumn(col);
        }
    }

    public void updateUserInfo() {
        if (user == null) {
            return;
        }
        LoggedInUser.setUser(user);
        greetingLabel.setText(String.format("Hello, %s.", user.getFirstName()));

        DefaultTableModel model = (DefaultTableModel) accountTable.getModel();
        model.setRowCount(0); // Delete all raws.

        accounts = accountController.getAllAccountsOfUser(user.getUserId());
        if (accounts == null || accounts.size() == 0) {
            return;
        }
        for (Account a : accounts) {
            if (a.isAccountDeleted() || a.isAccountLocked()) {
                continue;
            }
            model.addRow(new Object[]{
                    a.getAccountNumber(),
                    a.getType(),
                    a.getAccountCurrency(),
                    String.format("$ %.2f", (new AccountController()).getAccountBalance(a.getAccountNumber())),
            });
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    // DEBUG
    public static void main(String[] args) {
        UserView spy_user = new UserView();
        spy_user.setFirstName("Alice");
        spy_user.setUserId(1000007);

        MainView mainView = new MainView(); // For window dimension setup.
        UserLanding landing = new UserLanding(spy_user);
        MainView.setContentPanel(landing.getRootPanel());
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
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 6, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 6, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnCreateAccount = new JButton();
        btnCreateAccount.setText("Create a new account");
        rootPanel.add(btnCreateAccount, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        greetingLabel = new JLabel();
        greetingLabel.setText("Hello, <user>.");
        rootPanel.add(greetingLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Logout");
        rootPanel.add(logoutButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        rootPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "All accounts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        accountTable = new JTable();
        accountTable.setAutoCreateRowSorter(true);
        accountTable.setEnabled(true);
        accountTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        accountTable.setUpdateSelectionOnSort(false);
        scrollPane1.setViewportView(accountTable);
        viewCurrentCurrencyButton = new JButton();
        viewCurrentCurrencyButton.setText("View Current Currency");
        rootPanel.add(viewCurrentCurrencyButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addANewReceiverButton = new JButton();
        addANewReceiverButton.setText("Add a new Receiver");
        rootPanel.add(addANewReceiverButton, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewNotification = new JButton();
        viewNotification.setText("View Notifications");
        rootPanel.add(viewNotification, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        greetingLabel.setLabelFor(scrollPane1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
