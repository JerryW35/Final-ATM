package src.views;

import src.Database.Controllers.AccountController;
import src.Database.Models.Account;
import src.account.AccountFactory;
import src.user.LoggedInUser;
import src.user.UserView;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static src.views.TransactionPage.TYPE.*;

/**
 * The AccountPage class is a GUI that shows the details of a user's account and allows them to make transactions on the account
 */
public class AccountPage extends PageBack {
    private static final String LOAN_TYPE = "Loan";
    private static final String SECURITY_TYPE = "Securities";
    private JLabel balanceLabel;
    private JButton backButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transactionButton;
    private JButton closeAccountButton;
    private JButton viewHistoryButton;
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel accountNumberLabel;
    private JLabel accountNumberTextLabel;
    private JButton copyButton;
    private JButton enterStockMarketButton;
    private JLabel interestRateLabel;
    private UserView user;
    private Account account;
    private final StockMarket stockMarket = new StockMarket(false);

    public AccountPage(Account account) {
        this.account = account;
        this.updateLabels();
        depositButton.addActionListener(getTransactionActionListener(DEPOSIT));
        withdrawButton.addActionListener(getTransactionActionListener(WITHDRAW));
        transactionButton.addActionListener(getTransactionActionListener(TRANSACTION));
        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccountHistory history = new AccountHistory(account);
                history.setPreviousPanel(panel);
                MainView.setContentPanel(history.getRootPanel());
            }
        });
        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = (new AccountFactory()).closeAccount(account.getAccountNumber());
                if (result) {
                    MainView.removeAllPanel();
                    UserLanding userLanding = new UserLanding(user);
                    MainView.setContentPanel(userLanding.getRootPanel());
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainView.removeAllPanel();
                UserLanding userLanding = new UserLanding(user);
                MainView.setContentPanel(userLanding.getRootPanel());
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateLabels();
                super.componentShown(e);
            }
        });
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(accountNumberTextLabel.getText()), null
                );
            }
        });

        enterStockMarketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stockMarket.setPreviousPanel(panel);
                System.out.println(LoggedInUser.getUser().getUserId() + " " + account.getAccountNumber());
                stockMarket.setUser(LoggedInUser.getUser().getUserId(), account.getAccountNumber());
                MainView.setContentPanel(stockMarket.getRootPanel());
            }
        });
    }

    private void updateLabels() {
        accountNumberTextLabel.setText(String.valueOf(account.getAccountNumber()));
        titleLabel.setText("[" + account.getType() + "]");
        balanceLabel.setText(account.getAccountCurrency() +
                String.format(" $ %.2f", (new AccountController()).getAccountBalance(account.getAccountNumber())));
        if (account.getType().equalsIgnoreCase(LOAN_TYPE)) {
            depositButton.setVisible(false);
            depositButton.setEnabled(false);
            withdrawButton.setVisible(false);
            withdrawButton.setEnabled(false);
        }
        if (!account.getType().equalsIgnoreCase(SECURITY_TYPE)) {
            enterStockMarketButton.setVisible(false);
            enterStockMarketButton.setEnabled(false);
        }
        interestRateLabel.setText(
                String.format("%.2f", account.getInterestRate()) + "%");
    }

    private ActionListener getTransactionActionListener(TransactionPage.TYPE type) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransactionPage transactionPage = new TransactionPage(account);
                transactionPage.setPreviousPanel(panel);
                transactionPage.setType(type);
                MainView.setContentPanel(transactionPage.getPanel());
            }
        };
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    public JPanel getPanel() {
        return panel;
    }

    // DEBUG
    public static void main(String[] args) {
        Account fake_account = new Account();
        fake_account.setType("savings");
        fake_account.setAccountCurrency("EUR");
        fake_account.setBalance(1983.00);
        fake_account.setAccountNumber(1000000023);
        fake_account.setUserId(100008);

        MainView mainView = new MainView(); // For window dimension setup.
        AccountPage page = new AccountPage(fake_account);
        MainView.setContentPanel(page.getPanel());
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
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(11, 5, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 11, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 11, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        titleLabel = new JLabel();
        titleLabel.setText("[Checking] Account 1");
        panel.add(titleLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        balanceLabel = new JLabel();
        balanceLabel.setText("$ 0.00");
        panel.add(balanceLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        panel.add(toolBar$Separator1, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accountNumberLabel = new JLabel();
        accountNumberLabel.setText("Account Number:");
        panel.add(accountNumberLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accountNumberTextLabel = new JLabel();
        accountNumberTextLabel.setText("<account number>");
        panel.add(accountNumberTextLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        closeAccountButton = new JButton();
        closeAccountButton.setText("Close Account");
        panel.add(closeAccountButton, new com.intellij.uiDesigner.core.GridConstraints(9, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        withdrawButton = new JButton();
        withdrawButton.setText("Withdraw");
        panel.add(withdrawButton, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewHistoryButton = new JButton();
        viewHistoryButton.setText("View History");
        panel.add(viewHistoryButton, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyButton = new JButton();
        copyButton.setText("copy");
        panel.add(copyButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        panel.add(toolBar$Separator2, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterStockMarketButton = new JButton();
        enterStockMarketButton.setText("Enter Stock Market");
        panel.add(enterStockMarketButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Interest Rate:");
        panel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        interestRateLabel = new JLabel();
        interestRateLabel.setText("<interest rate>");
        panel.add(interestRateLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        depositButton = new JButton();
        depositButton.setText("Deposit");
        panel.add(depositButton, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transactionButton = new JButton();
        transactionButton.setText("Transaction");
        panel.add(transactionButton, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
