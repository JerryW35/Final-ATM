package src.views.formatter;

import java.util.Date;

public class DateView {
    public static String formatDate(Date date) {
        String[] s = date.toString().split(" ");
        return String.format("%s %s %s, %s", s[0], s[1], s[2], s[5]);
    }

    public static String formatTime(Date date) {
        return date.toString().split(" ")[1];
    }
}
