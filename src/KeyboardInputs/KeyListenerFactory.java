package src.KeyboardInputs;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
Factory pattern implemented to get key inputs and everytime the same is done we are handling the inputs on our end
Please note that this class has been made with reference to a doubt on SOF and might have similarities to the same. Hope the same can be ignored
 */
public class KeyListenerFactory {
    public static KeyAdapter getIntegerKeyListener(JTextField tf, JLabel label, int min, int max) {
        // Reference: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
        return new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                String value = tf.getText();
                char c = ke.getKeyChar();
                if ((c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_BACK_SPACE)) {
                    label.setText("");
                } else if (!Character.isDigit(c)) {
                    ke.consume();
                } else if (Integer.parseInt(value + c) < min) {
                    label.setText("* Enter a valid value in the range.");
                } else if (Integer.parseInt(value + c) > max) {
                    ke.consume();
                } else {
                    label.setText("");
                }
            }
        };
    }

    public static KeyAdapter getDoubleKeyListener(JTextField tf, JLabel label, double min, double max) {
        // TODO: allow at most two digits float.
        return new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                String value = tf.getText();
                char c = ke.getKeyChar();
                if ((c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_BACK_SPACE)) {
                    label.setText("");
                }
                try {
                    double data = Double.parseDouble(value + c);
                    if (data < min) {
                        label.setText("* Enter a valid value in the range.");
                    } else if (data > max) {
                        ke.consume();
                    } else {
                        label.setText("");
                    }
                } catch (NumberFormatException e) {
                    ke.consume();
                }
            }
        };
    }
}
