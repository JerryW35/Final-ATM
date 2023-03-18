/*
this class is used for checking inputs format
create by Jerry
 */
package src.KeyboardInputs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/*
Singleton validation class to check validations of inputs that the user provides.
All validations takes place here to check key inputs
 */

public class KeyboardInputs {
    public static  final  int pwdDigits=8;
    public static  final  int pwdTypes=3;
    public static final int usernameLength=4;
    public static boolean checkIfGivenString(String input,String toCheck) {
        return input.equalsIgnoreCase(toCheck);
    }
    public static boolean checkIfDate(String dateStr) throws ParseException {
        boolean retBool= true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            format.setLenient(false);
            Date date = format.parse(dateStr);
        }catch (Exception e){
            retBool=false;
        }
        String yearStr=dateStr.split("-")[0];
        if(yearStr.startsWith("0") || yearStr.length()!=4){
            retBool=false;
        }
        return retBool;
    }

    public static boolean checkIfEmail(String emailStr){
        // use regular expression to check
        if (emailStr == null || emailStr.length() < 1 || emailStr.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(emailStr).matches();
    }
    public static boolean checkIfPassword(String password){
        //check if password includes 3 kinds of type: upper letters, lower letters, number ,special symbols
        // the passowrd length >= pwdDigits
        final String REG_NUMBER = ".*\\d+.*";
        final String REG_UPPERCASE = ".*[A-Z]+.*";
        final String REG_LOWERCASE = ".*[a-z]+.*";
        final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
        if (password == null || password.length() <pwdDigits ) return false;
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE))i++;
        if (password.matches(REG_UPPERCASE)) i++;
        if (password.matches(REG_SYMBOL)) i++;
        if (i  < pwdTypes )  return false;
        return true;
    }
    public static boolean checkIfName(String nameStr){
        return nameStr.matches("[a-zA-Z,_-]+")&&nameStr.length()>=3;
    }
    public static boolean checkIfUserName(String nameStr){
        return nameStr.matches("^[a-z0-9A-Z,._-]+$")&&nameStr.length()>=usernameLength;
    }
    public static boolean checkIfOnlyString(String Str){
        return Str.matches("[a-zA-Z]+");
    }
    public static boolean checkIfZipCode(String Str){
        return Str.chars().allMatch(Character::isDigit) && Str.length() == 5;
    }

    public static boolean checkIfGivenStringinList(String toCheck,List<String> options) {
        return options.contains(toCheck);
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(KeyboardInputs.checkIfDate("2022-01-01"));
    }

}
