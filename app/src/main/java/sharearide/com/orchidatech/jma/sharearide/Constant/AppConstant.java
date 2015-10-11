package sharearide.com.orchidatech.jma.sharearide.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppConstant {

    public static long User_ID = 0;
    public static boolean isInSetting = false;

    public enum AppDrawer {
        Home(10),
        Favorites(20),
        NearOffers(30),
        Settings(1000);
        public int id;

        private AppDrawer(int id) {
            this.id = id;
        }
    }


    public static class SharedPreferenceNames {
        public static String SocialUser = "SocialUser";
    }


    public static class DateConvertion {
        final static String MONTHS[] = new String[]{"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

        public static Date getCurrentDate() {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = new Date((long) 1379487711 * 1000);
            return currenTimeZone;
        }

        public static Date getDateFromString(String strDate) {
            long milliseconds = Long.parseLong(strDate);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(milliseconds);
            return c.getTime();
        }

        public static String getDate(long milliSeconds)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            String fullDate = buildValueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " "+MONTHS[calendar.get(Calendar.MONTH)]+ " " + calendar.get(Calendar.YEAR) + " at "+
                    buildValueOf(getHour(calendar.get(Calendar.HOUR_OF_DAY))) + ":" + buildValueOf(calendar.get(Calendar.MINUTE)) + " " + time_type(calendar.get(Calendar.HOUR_OF_DAY));
            return fullDate;
        }

        private static String time_type(int i) {
            if(i > 12)
                return "PM";

            return "AM";
        }

        private static int getHour(int hour) {
            if(hour < 12)
                return hour;
            else
                return hour-12;
        }

        private static String buildValueOf(int value) {
            if (value >= 10)
                return String.valueOf(value);
            else
                return "0" + String.valueOf(value);
        }
    }
}
