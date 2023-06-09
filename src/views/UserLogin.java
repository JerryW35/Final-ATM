package src.views;

import src.Helpers.VirtualDateHandler;
import src.user.LoggedInUser;
import src.user.Login;
import src.user.UserView;
import src.views.formatter.DateView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static src.KeyboardInputs.KeyboardInputs.checkIfPassword;

/**
 * This is the UserLogin class, which provides a user interface for logging in to the system.
 * It contains methods for handling sign up and login actions.
 */
public class UserLogin {
    private static final String FRAME_TITLE = "Login Page";
    private static JFrame jframe;
    private JFormattedTextField username;
    private JPasswordField password;
    private JPanel rootPanel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton signupButton;
    private JButton loginButton;
    private JLabel passwordErrorLabel;
    private JLabel errorLabel;
    private JLabel titleImageLabel;
    private JLabel dateLabel;
    private UserView validUser;

    public UserLogin() {
        $$$setupUI$$$();
        dateLabel.setText(DateView.formatDate(VirtualDateHandler.getCurrentDate()));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainView.setContentPanel((new UserSignup()).getRootPanel());
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password_s = String.valueOf(password.getPassword());
                if (!checkIfPassword(password_s)) {
                    passwordErrorLabel.setText("* Your password is in the invalid format.");
                    return;
                }
                validUser = (new Login()).login(username.getText(), password_s);
                if (validUser == null) {
                    errorLabel.setText("* Your username or password is wrong.");
                    return;
                }
                LoggedInUser.setUser(validUser);

                MainView.removeAllPanel();
                if (validUser.isManager()) {
                    ManagerLanding managerLanding = new ManagerLanding();
                    managerLanding.setPreviousPanel(rootPanel);
                    MainView.setContentPanel(managerLanding.getRootPanel());
                } else {
                    UserLanding userLanding = new UserLanding(validUser);
                    userLanding.setPreviousPanel(rootPanel);
                    MainView.setContentPanel(userLanding.getRootPanel());
                }
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    // DEBUG
    public static void main(String[] args) {
        MainView mainView = new MainView(); // For window dimension setup.
        UserLogin login = new UserLogin();
        MainView.setContentPanel(login.getRootPanel());
    }

    private void createUIComponents() {
        try {
            // Reference: https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
            BufferedImage myPicture = ImageIO.read(new File("img/banner.png"));
            titleImageLabel = new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            titleImageLabel = new JLabel();
        }
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
        username = new JFormattedTextField();
        username.setText("");
        rootPanel.add(username, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(330, 30), null, 0, false));
        password = new JPasswordField();
        password.setText("");
        rootPanel.add(password, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(330, 27), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        rootPanel.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(62, 27), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 10, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        signupButton = new JButton();
        signupButton.setText("Signup");
        rootPanel.add(signupButton, new com.intellij.uiDesigner.core.GridConstraints(8, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginButton = new JButton();
        loginButton.setText("Login");
        rootPanel.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("Username:");
        rootPanel.add(usernameLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        rootPanel.add(toolBar$Separator1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordErrorLabel = new JLabel();
        passwordErrorLabel.setForeground(new Color(-65536));
        passwordErrorLabel.setText("");
        rootPanel.add(passwordErrorLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-65536));
        errorLabel.setText("");
        rootPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titleImageLabel.setText("");
        rootPanel.add(titleImageLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateLabel = new JLabel();
        dateLabel.setText("<date>");
        rootPanel.add(dateLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
