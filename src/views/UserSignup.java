package src.views;

import src.Database.Controllers.UserController;
import src.Database.Models.User;
import src.KeyboardInputs.KeyListenerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;

import static src.KeyboardInputs.KeyboardInputs.*;

/**
 * The UserSignup class is a GUI that allows a user to create an account with the bank
 */
public class UserSignup extends PageBack {
    private static final String FRAME_TITLE = "Customer Signup Page";
    private static final int MIN_YEAR = 1900;
    private JFormattedTextField firstNameTextField;
    private JFormattedTextField lastNameTextField;
    private JFormattedTextField ssnTextField;
    private JLabel midNameLabel;
    private JLabel firstNameLabel;
    private JLabel dateOfBirthLabel;
    private JLabel ssnLabel;
    private JButton btnSubmit;
    private JPanel rootPanel;
    private JProgressBar progressBar1;
    private JButton btnBackToLogin;
    private JFormattedTextField usernameTextField;
    private JPasswordField passwordField;
    private JFormattedTextField yearTextField;
    private JComboBox monthComboBox;
    private JFormattedTextField dayTextField;
    private JLabel yearErrorLabel;
    private JLabel dayErrorLabel;
    private JLabel ssnErrorLabel;
    private JLabel passwordErrorLabel;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JRadioButton personalBankingRadioButton;
    private JRadioButton businessBankingRadioButton;
    private JLabel usernameErrorLabel;
    private JLabel firstNameErrorLabel;
    private JLabel lastNameErrorLabel;
    private JLabel typeErrorLabel;
    private JFormattedTextField emailTextField;
    private JLabel emailErrorLabel;
    private JLabel emailLabel;
    private JFormattedTextField phoneTextField;
    private JLabel phoneLabel;
    private User user = new User();

    public UserSignup() {
        initTextFieldFormat();
        initBtnListeners();
        personalBankingRadioButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                typeErrorLabel.setText("");
                user.setCompany(false);
            }
        });
        businessBankingRadioButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                typeErrorLabel.setText("");
                user.setCompany(true);
            }
        });
        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainView.removeAllPanel();
                MainView.setContentPanel((new UserLogin()).getRootPanel());
            }
        });
    }

    private void initTextFieldFormat() {
        usernameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                String username = usernameTextField.getText();
                if (!checkIfUserName(username + ke.getKeyChar())) {
                    usernameErrorLabel.setText("* Required a udsername with at least " +
                            usernameLength + " letters and no special character please.");
                } else if ((new UserController()).checkIfUserExists(username + ke.getKeyChar())) {
                    usernameErrorLabel.setText("* This username has been used by others. Please change to a new one");
                } else {
                    usernameErrorLabel.setText("");
                }
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String pwd = String.valueOf(passwordField.getPassword());
                if (!checkIfPassword(pwd + e.getKeyChar())) {
                    passwordErrorLabel.setText("<html>" +
                            "* Required a password with at least " + pwdDigits + " characters.<br/>" +
                            "Enter " + pwdTypes + " of these types: upper letters, lower letters, number, and special symbols." +
                            "</html>");
                } else {
                    passwordErrorLabel.setText("");
                }
            }
        });

        emailTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (checkIfEmail(emailTextField.getText() + e.getKeyChar())) {
                    emailErrorLabel.setText("");
                } else {
                    emailErrorLabel.setText("* Please enter a valid email.");
                }
            }
        });

        yearTextField.addKeyListener(KeyListenerFactory.getIntegerKeyListener(yearTextField, yearErrorLabel, MIN_YEAR, Year.now().getValue()));
        dayTextField.addKeyListener(KeyListenerFactory.getIntegerKeyListener(dayTextField, dayErrorLabel, 1, 31));
        ssnTextField.addKeyListener(KeyListenerFactory.getIntegerKeyListener(ssnTextField, ssnErrorLabel, 100000000, 999999999));
    }

    private void initBtnListeners() {
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfUserName(usernameTextField.getText())) {
                    user.setUserName(usernameTextField.getText());
                    usernameErrorLabel.setText("");
                } else {
                    usernameErrorLabel.setText("* Please type a valid username.");
                    return;
                }

                if (checkIfPassword(String.valueOf(passwordField.getPassword()))) {
                    user.setPassword(String.valueOf(passwordField.getPassword()));
                    passwordErrorLabel.setText("");
                } else {
                    passwordErrorLabel.setText("* Please type a valid password.");
                    return;
                }

                if (checkIfName(firstNameTextField.getText())) {
                    user.setFirstName(firstNameTextField.getText());
                    firstNameErrorLabel.setText("");
                } else {
                    firstNameErrorLabel.setText("* Please double check your first name.");
                    return;
                }

                if (checkIfName(lastNameTextField.getText())) {
                    user.setLastName(lastNameTextField.getText());
                    lastNameErrorLabel.setText("");
                } else {
                    lastNameErrorLabel.setText("* Please double check your last name.");
                    return;
                }

                if (businessBankingRadioButton.isSelected() || personalBankingRadioButton.isSelected()) {
                    typeErrorLabel.setText("");
                } else {
                    typeErrorLabel.setText("* Please select the type of user to create.");
                    return;
                }

                try {
                    user.setDateOfBirth(Date.valueOf(LocalDate.of(
                            Integer.parseInt(yearTextField.getText()),
                            monthComboBox.getSelectedIndex() + 1,
                            Integer.parseInt(dayTextField.getText())
                    )));
                } catch (DateTimeException | NumberFormatException _e) {
                    System.out.println(_e);
                    dayErrorLabel.setText("Please enter a valid date.");
                    return;
                }

                if (checkIfEmail(emailTextField.getText())) {
                    user.setEmail(emailTextField.getText());
                    emailErrorLabel.setText("");
                } else {
                    emailErrorLabel.setText("* Please enter a valid email.");
                    return;
                }

                // All users created account here are customers.
                user.setManager(false);
                user.setSocialSecurity(ssnTextField.getText());
                user.setContactNumber(phoneTextField.getText());

                // go to the next page of the signup process.
                ContactInformation contactInformation = new ContactInformation();
                contactInformation.setup(user, ContactInformation.AddressType.PERMANENT);
                contactInformation.setPreviousPanel(rootPanel);
                MainView.setContentPanel(contactInformation.getPanel());
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    // DEBUG
    public static void main(String[] args) {
        MainView mainView = new MainView(); // For window dimension setup.
        UserSignup signup = new UserSignup();
        MainView.setContentPanel(signup.getRootPanel());
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
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(26, 7, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(25, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        firstNameTextField = new JFormattedTextField();
        firstNameTextField.setText("");
        rootPanel.add(firstNameTextField, new com.intellij.uiDesigner.core.GridConstraints(10, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        firstNameLabel = new JLabel();
        firstNameLabel.setText("First name");
        rootPanel.add(firstNameLabel, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ssnTextField = new JFormattedTextField();
        rootPanel.add(ssnTextField, new com.intellij.uiDesigner.core.GridConstraints(18, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        midNameLabel = new JLabel();
        midNameLabel.setText("Last name");
        rootPanel.add(midNameLabel, new com.intellij.uiDesigner.core.GridConstraints(12, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateOfBirthLabel = new JLabel();
        dateOfBirthLabel.setText("Date of Birth");
        rootPanel.add(dateOfBirthLabel, new com.intellij.uiDesigner.core.GridConstraints(14, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ssnLabel = new JLabel();
        ssnLabel.setText("Social Security Number");
        rootPanel.add(ssnLabel, new com.intellij.uiDesigner.core.GridConstraints(18, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSubmit = new JButton();
        btnSubmit.setText("Next");
        rootPanel.add(btnSubmit, new com.intellij.uiDesigner.core.GridConstraints(24, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        rootPanel.add(toolBar$Separator1, new com.intellij.uiDesigner.core.GridConstraints(23, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progressBar1 = new JProgressBar();
        rootPanel.add(progressBar1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBackToLogin = new JButton();
        btnBackToLogin.setText("Back to Login");
        rootPanel.add(btnBackToLogin, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        rootPanel.add(usernameLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        rootPanel.add(toolBar$Separator2, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameTextField = new JFormattedTextField();
        usernameTextField.setText("");
        rootPanel.add(usernameTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setText("");
        rootPanel.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        rootPanel.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        yearTextField = new JFormattedTextField();
        yearTextField.setText("");
        yearTextField.setToolTipText("The year of your birthday.");
        rootPanel.add(yearTextField, new com.intellij.uiDesigner.core.GridConstraints(14, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        monthComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("January");
        defaultComboBoxModel1.addElement("February");
        defaultComboBoxModel1.addElement("March");
        defaultComboBoxModel1.addElement("April");
        defaultComboBoxModel1.addElement("May");
        defaultComboBoxModel1.addElement("June");
        defaultComboBoxModel1.addElement("July");
        defaultComboBoxModel1.addElement("August");
        defaultComboBoxModel1.addElement("September");
        defaultComboBoxModel1.addElement("October");
        defaultComboBoxModel1.addElement("November");
        defaultComboBoxModel1.addElement("December");
        monthComboBox.setModel(defaultComboBoxModel1);
        rootPanel.add(monthComboBox, new com.intellij.uiDesigner.core.GridConstraints(14, 3, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        yearErrorLabel = new JLabel();
        yearErrorLabel.setForeground(new Color(-65536));
        yearErrorLabel.setText("");
        rootPanel.add(yearErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(16, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dayErrorLabel = new JLabel();
        dayErrorLabel.setForeground(new Color(-65536));
        dayErrorLabel.setText("");
        rootPanel.add(dayErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(17, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ssnErrorLabel = new JLabel();
        ssnErrorLabel.setForeground(new Color(-65536));
        ssnErrorLabel.setText("");
        rootPanel.add(ssnErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(19, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordErrorLabel = new JLabel();
        passwordErrorLabel.setForeground(new Color(-65536));
        passwordErrorLabel.setText("");
        rootPanel.add(passwordErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Type of User");
        rootPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        personalBankingRadioButton = new JRadioButton();
        personalBankingRadioButton.setText("Personal Banking");
        rootPanel.add(personalBankingRadioButton, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameErrorLabel = new JLabel();
        usernameErrorLabel.setForeground(new Color(-65536));
        usernameErrorLabel.setText("");
        rootPanel.add(usernameErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstNameErrorLabel = new JLabel();
        firstNameErrorLabel.setForeground(new Color(-65536));
        firstNameErrorLabel.setText("");
        rootPanel.add(firstNameErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(11, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastNameErrorLabel = new JLabel();
        lastNameErrorLabel.setForeground(new Color(-65536));
        lastNameErrorLabel.setText("");
        rootPanel.add(lastNameErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(13, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(1, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lastNameTextField = new JFormattedTextField();
        lastNameTextField.setText("");
        rootPanel.add(lastNameTextField, new com.intellij.uiDesigner.core.GridConstraints(12, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        typeErrorLabel = new JLabel();
        typeErrorLabel.setForeground(new Color(-65536));
        typeErrorLabel.setText("");
        rootPanel.add(typeErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailLabel = new JLabel();
        emailLabel.setText("E-mail Address");
        rootPanel.add(emailLabel, new com.intellij.uiDesigner.core.GridConstraints(20, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailTextField = new JFormattedTextField();
        emailTextField.setText("");
        rootPanel.add(emailTextField, new com.intellij.uiDesigner.core.GridConstraints(20, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        emailErrorLabel = new JLabel();
        emailErrorLabel.setForeground(new Color(-65536));
        emailErrorLabel.setText("");
        rootPanel.add(emailErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(21, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phoneLabel = new JLabel();
        phoneLabel.setText("Phone Number");
        rootPanel.add(phoneLabel, new com.intellij.uiDesigner.core.GridConstraints(22, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phoneTextField = new JFormattedTextField();
        rootPanel.add(phoneTextField, new com.intellij.uiDesigner.core.GridConstraints(22, 2, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Year");
        rootPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(15, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Date");
        rootPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(15, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Month");
        rootPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(15, 3, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dayTextField = new JFormattedTextField();
        dayTextField.setText("");
        dayTextField.setToolTipText("The day in the month of your birthday.");
        rootPanel.add(dayTextField, new com.intellij.uiDesigner.core.GridConstraints(14, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        businessBankingRadioButton = new JRadioButton();
        businessBankingRadioButton.setText("Business Banking");
        rootPanel.add(businessBankingRadioButton, new com.intellij.uiDesigner.core.GridConstraints(7, 4, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(personalBankingRadioButton);
        buttonGroup.add(businessBankingRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}

