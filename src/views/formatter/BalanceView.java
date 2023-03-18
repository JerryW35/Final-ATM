package src.views.formatter;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class BalanceView {
    public static JFormattedTextField getMoneyTextField() {
        NumberFormatter formatter = new NumberFormatter();
        formatter.setAllowsInvalid(false);

        return new JFormattedTextField(formatter);
    }
}
