package src.Helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
Through the progrram we are managing a virtual date which is used to generate interests etc
Virtual Date handler is a singleton class which manages updates and release this date.
 */
public class VirtualDateHandler {
    public static Date currentDate;

    VirtualDateHandler(){
        try {
            FileReader reader = new FileReader("virtualDate.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineReader;
            String date = null;

            while ((lineReader = bufferedReader.readLine()) != null) {
                date = lineReader;
                break;
            }
            reader.close();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            currentDate = formatter.parse(date);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateDate(){
        try {
            FileReader reader = new FileReader("src/Helpers/VirtualDate.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineReader;
            String date = null;

            while ((lineReader = bufferedReader.readLine()) != null) {
                date = lineReader;
                break;
            }
            reader.close();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            currentDate = formatter.parse(date);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeDate(){
        updateDate();
        currentDate = new Date(currentDate.getTime() + DateConverter.MILLIS_IN_A_DAY);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            FileWriter writer = new FileWriter("src/Helpers/VirtualDate.txt", false);
            writer.write(formatter.format(currentDate));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Date getCurrentDate() {
        updateDate();
        return currentDate;
    }
}
