package sharearide.com.orchidatech.jma.sharearide.Chat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Chat.client.Constants;


/**
 * @author appsrox.com
 */
public class Common extends Application {

    public static final String PROFILE_ID = "profile_id";

    public static final String ACTION_REGISTER = "sharearide.com.orchidatech.jma.sharearide.REGISTER";
    public static final String EXTRA_STATUS = "status";
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 0;

    //parameters recognized by demo server
    public static final String FROM = "email";
    public static final String REG_ID = "regId";
    public static final String MSG = "msg";
    public static final String TO = "email2";

    public static String[] email_arr;

    private static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        List<String> emailList = getEmailList();
        email_arr = emailList.toArray(new String[emailList.size()]);


        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("sharearide.db").create();

        /**
         * here we declare models
         */
        //Configuration.Builder configBuilder = new Configuration.Builder(this);
        //configBuilder.addModelClasses(Ride.class, User.class, Chat.class);

        ActiveAndroid.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());


    }

    private List<String> getEmailList() {
        List<String> lst = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                lst.add(account.name);
            }
        }
        return lst;
    }

    public static String getPreferredEmail() {
        return prefs.getString("chat_email_id", email_arr.length == 0 ? "abc@example.com" : email_arr[0]);
    }

    public static String getDisplayName() {
        String email = getPreferredEmail();
        return prefs.getString("display_name", email.substring(0, email.indexOf('@')));
    }

    public static boolean isNotify() {
        return prefs.getBoolean("notifications_new_message", true);
    }

    public static String getRingtone() {
        return prefs.getString("notifications_new_message_ringtone", android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }

    public static String getServerUrl() {
        return prefs.getString("server_url_pref", Constants.SERVER_URL);
    }

    public static String getSenderId() {
        return prefs.getString("sender_id_pref", Constants.SENDER_ID);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
