package sharearide.com.orchidatech.jma.sharearide.Constant;

import android.util.Log;


public class AppLog {

    public static final String TAG = "MyApp";

    public static void i(String msg) {
        Log.i(TAG, msg);
        //Crashlytics.log(msg);
    }

}
