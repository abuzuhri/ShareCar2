package sharearide.com.orchidatech.jma.sharearide.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;


public class SplashScreen extends ActionBarActivity {
    private static final long INITIAL_SERVICE_DELAY = 120 * 1000L;//FOR ONE MINUTE
    private Timer mTimer;
    private long user_id;
    Intent intent;
private  ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

//       UserDAO.deleteAllUsers();
//       RideDAO.deleteAllRides();
        user_id =  getSharedPreferences("pref", MODE_PRIVATE).getLong("id",-1);
        logo=(ImageView)findViewById(R.id.logo);

        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().width =(int)(height*0.5);
        logo.getLayoutParams().height =(int)(height*0.45);


        if (user_id == -1)
            intent = new Intent(SplashScreen.this, Login.class);
        else{
            User user = UserDAO.getUserById(user_id);
            if(TextUtils.isEmpty(user.getEmail()) || TextUtils.isEmpty(user.getPhone()))
                intent = new Intent(SplashScreen.this, UserProfile.class);
            else
                intent = new Intent(SplashScreen.this, ShareRide.class);

        }

        // setTimerForRefreshRideService();
//         this.startService(new Intent(this, RefreshRideService.class));


        if (SplashScreen.this.getSharedPreferences("pref", MODE_PRIVATE).getBoolean("FIRST_TIME", true)) {

            InternetConnectionChecker.isConnectedToInternet(SplashScreen.this, new OnInternetConnectionListener() {
                @Override
                public void internetConnectionStatus(boolean status) {


                    if (status) {
                        Toast.makeText(SplashScreen.this, "Loading Data...", Toast.LENGTH_SHORT).show();
                        loadNewRides(SplashScreen.this);


                    } else
                        Toast.makeText(SplashScreen.this, "No Internet Access", Toast.LENGTH_SHORT).show();

                    SplashScreen.this.getSharedPreferences("pref", MODE_PRIVATE).edit().putBoolean("FIRST_TIME", false).commit();
                    goToTargetActivity();
                }
            });
        }else
            goToTargetActivity();
    }

    private void goToTargetActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

//    private void setTimerForRefreshRideService() {
//        mTimer = new Timer();
//        TimerTask mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                InternetConnectionChecker.isConnectedToInternet(SplashScreen.this, new OnInternetConnectionListener() {
//                    @Override
//                    public void internetConnectionStatus(boolean status) {
//                        if (status) {
//                            Intent intent = new Intent(SplashScreen.this, RefreshRideService.class);
//                            startService(intent);
//
//                        }
//                    }
//                });
//            }
//        };
//
//        mTimer.scheduleAtFixedRate(mTimerTask, new Date(System.currentTimeMillis() + INITIAL_SERVICE_DELAY), INITIAL_SERVICE_DELAY);
//        // mTimer.scheduleAtFixedRate(mTimerTask, INITIAL_SERVICE_DELAY, INITIAL_SERVICE_DELAY);
//        //timer.schedule(mTimerTask, new Date(System.currentTimeMillis() + INITIAL_SERVICE_DELAY));
//
//
//    }
//

    public void loadNewRides(Context context){
        MainUserFunctions.get_a_rides(context, new OnRidesListListener() {
            @Override
            public void onRidesRefresh(ArrayList<Ride> newItems) {
                for(Ride ride:newItems)
                    RideDAO.addNewRide(ride.getRemoteId(), ride.getUserId(), ride.getFromCity(), ride.getToCity(), ride.getFromCountry(), ride.getToCountry(), ride.getFromState(), ride.getToState(),
                            ride.getDateTime(), ride.getCost(), ride.getMore_info(),ride.getFrom_Longitude(), ride.getTo_longitude(), ride.getFrom_Lattitude(), ride.getTo_latitude());
            }

            @Override
            public void onRidesRefreshFailed(String error) {

            }
        });

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
