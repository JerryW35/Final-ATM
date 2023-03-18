package src;

import src.views.MainView;
import src.views.UserLogin;

public class Main {
    private static final MainView mainView = new MainView(); // For window dimension setup.
    private static final UserLogin login = new UserLogin();

    public static void main(String[] args) {
            MainView.setContentPanel(login.getRootPanel());
    }
}