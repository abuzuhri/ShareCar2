package sharearide.com.orchidatech.jma.sharearide.Database;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;

/**
 * Created by Shadow on 9/6/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("sharearide.db").create();

        /**
         * here we declare models
         */
        //Configuration.Builder configBuilder = new Configuration.Builder(this);
        //configBuilder.addModelClasses(Ride.class, User.class, Chat.class);

        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ActiveAndroid.dispose();
    }
}
