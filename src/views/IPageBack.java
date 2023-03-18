package src.views;

import javax.swing.*;

/**
 This interface is for a page to be able to turn back to previous panel.
 */
public interface IPageBack {
    void setPreviousPanel(JPanel panel);
    JPanel getPreviousPanel();
}
