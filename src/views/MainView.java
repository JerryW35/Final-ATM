package src.views;

import javax.swing.*;
import java.awt.*;

/**
 This is the MainView class, which represents the main window of the application.
 It contains constants for the frame dimensions and title, as well as fields for each page that can be shown in the window.
 The class also has methods for setting the content panel and removing all panels from the window.
 */
public class MainView {
    private static final String APP_TITLE = "My Bank ATM";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 900;
    protected static final JFrame frame = new JFrame(APP_TITLE);

    public static final UserLogin userLogin = new UserLogin();

    public static final UserSignup userSignup = new UserSignup();
    public static final ContactInformation contactInformation = new ContactInformation();

    public static final AccountCreation accountCreation = new AccountCreation();

    public MainView() {
        frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    public static int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public static String getAppTitle() {
        return APP_TITLE;
    }

    public static void setContentPanel(JPanel panel) {
        panel.updateUI();
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void removeAllPanel() {
        frame.getContentPane().removeAll();
    }

    // DEBUG
}
