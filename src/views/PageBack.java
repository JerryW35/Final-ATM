package src.views;

import javax.swing.*;

/**
 This abstract class provides an implementation for a page to head back to the previous page.
 */
public abstract class PageBack implements IPageBack {
    private JPanel previousPanel;
    @Override
    public void setPreviousPanel(JPanel panel) {
        panel.setVisible(false);
        this.previousPanel = panel;
    }

    @Override
    public JPanel getPreviousPanel() {
        return previousPanel;
    }

    public void headBack() {
        getPreviousPanel().setVisible(true);
        MainView.setContentPanel(getPreviousPanel());
    }
}
