package sharearide.com.orchidatech.jma.sharearide.Logic;

/**
 * Created by Amal on 10/7/2015.
 */

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by israa on 10/6/2015.
 */
public  class Validation {
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhoneNumber(String s){
        Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches());
        return true;}

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;



    }
    public static boolean passwordcontainsNumber(String s)  {
        boolean result = false;
        byte[] bytes = new byte[0];
        try {
            bytes = s.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int tmp, i = bytes.length;
        while (i >0  && (true | (result |= ((tmp = bytes[--i] - '0') >= 0) && tmp <= 9)));
        return result;
    }

}
