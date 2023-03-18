package src.views;

import src.Database.Controllers.AccountInterestController;
import src.Database.Models.Account;
import src.account.ProcessData;
import src.user.UserView;
import src.views.formatter.BalanceView;
import src.views.formatter.CurrencyView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * The AccountCreation class is a GUI that allows a user to create a new account
 */
public class AccountCreation extends PageBack {
    private static final String FRAME_TITLE = "Create New Account";
    private static final ProcessData processor = new ProcessData();
    private JFormattedTextField initialDeposit;
    private JButton btnSubmit;
    private JComboBox<String> accountTypes;
    private JLabel accountLabel;
    private JPanel rootPanel;
    private JComboBox<String> currencyComboBox;
    private JLabel currencyLabel;
    private JLabel depositLabel;
    private JButton cancelButton;
    private JLabel errorLabel;
    private JLabel amountErrorLabel;
    private JLabel interestRateLabel;
    private UserView user;
    private String accountType;
    private static final AccountInterestController interestController = new AccountInterestController();

    public AccountCreation() {
        $$$setupUI$$$();
        updateInterestLabel();
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("");
                headBack();
            }
        });
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account account = new Account();
                account.setUserId(user.getUserId());
                try {
                    double amount = Double.parseDouble(initialDeposit.getText().replace(",", ""));
                    if (amount < 0) {
                        errorLabel.setText("* Please deposit first.");
                        return;
                    }
                    account.setBalance(amount);
                } catch (NumberFormatException _e) {
                    errorLabel.setText("* Please enter a valid amount.");
                    return;
                }

                String currency = currencyComboBox.getItemAt(currencyComboBox.getSelectedIndex());
                account.setAccountCurrency(currency.substring(0, 3));

                account.setType(accountType);

                boolean result = processor.process(user, accountType, account);
                if (result) {
                    MainView.removeAllPanel();
                    UserLanding userLanding = new UserLanding(user);
                    MainView.setContentPanel(userLanding.getRootPanel());
                } else {
                    errorLabel.setText("Something went wrong.");
                }
            }
        });
        accountTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (accountTypes.getSelectedIndex()) {
                    case 3:
                        // Loan
                        depositLabel.setText("How much to borrow at first?");
                    case 2:
                        // Securities
                        currencyComboBox.setSelectedItem(CurrencyView.formatCurrency("USD"));
                        currencyComboBox.setEnabled(false);
                        break;
                    default:
                        depositLabel.setText("How much to deposit at first?");
                        currencyComboBox.setEnabled(true);
                }
                updateInterestLabel();
            }
        });
    }

    private void updateInterestLabel() {
        accountType = getSelectedAccountType();
        interestRateLabel.setText(
                String.format("%.2f", interestController.returnAccountInterestRate(accountType))
                        + " %");
    }

    private String getSelectedAccountType() {
        switch (accountTypes.getSelectedIndex()) {
            case 0: // Checking
                return "Checking";
            case 1: // Saving
                return "Savings";
            case 2: // Security
                return "Securities";
            case 3: // Loan
                return "Loan";
            default:
        }
        return null;
    }

    protected String getFrameTitle() {
        return FRAME_TITLE;
    }

    protected JPanel getPanel() {
        return rootPanel;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    private void createUIComponents() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(CurrencyView.getCurrencyList());
        currencyComboBox = new JComboBox<>(model);

        initialDeposit = BalanceView.getMoneyTextField();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 5, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 10, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 10, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rootPanel.add(initialDeposit, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        accountTypes = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Checking");
        defaultComboBoxModel1.addElement("Saving");
        defaultComboBoxModel1.addElement("Security");
        defaultComboBoxModel1.addElement("Loan");
        accountTypes.setModel(defaultComboBoxModel1);
        rootPanel.add(accountTypes, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accountLabel = new JLabel();
        accountLabel.setText("Which account to create?");
        rootPanel.add(accountLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        rootPanel.add(toolBar$Separator1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currencyLabel = new JLabel();
        currencyLabel.setText("Which currency to use?");
        rootPanel.add(currencyLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        depositLabel = new JLabel();
        depositLabel.setText("How much to deposit at first?");
        rootPanel.add(depositLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSubmit = new JButton();
        btnSubmit.setText("Submit");
        rootPanel.add(btnSubmit, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        rootPanel.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-65536));
        errorLabel.setText("");
        rootPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        amountErrorLabel = new JLabel();
        amountErrorLabel.setForeground(new Color(-65536));
        amountErrorLabel.setText("");
        rootPanel.add(amountErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rootPanel.add(currencyComboBox, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Current interest rate:");
        rootPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        interestRateLabel = new JLabel();
        interestRateLabel.setText("<interest rate>");
        rootPanel.add(interestRateLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("USD $");
        rootPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
