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
    }
}
