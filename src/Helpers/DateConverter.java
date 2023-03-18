package src.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
This class follows a singleton pattern to convert date type from sql to viewing date type using the formatter
and also allows adding of date by using static final variables
 */
public class DateConverter {

    public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    public static final long MILLIS_IN_A_YEAR = 1000 * 60 * 60 * 24*365;
    public static Date convertStringToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
