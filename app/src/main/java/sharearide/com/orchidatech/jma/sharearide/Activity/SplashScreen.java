package sharearide.com.orchidatech.jma.sharearide.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Services.RefreshRideService;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;


public class SplashScreen extends ActionBarActivity {
    private static final long INITIAL_SERVICE_DELAY = 120 * 1000L;//FOR ONE MINUTE
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setTimerForRefreshRideService();
        InternetConnectionChecker.isConnectedToInternet(SplashScreen.this, new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                final Intent intent = new Intent(SplashScreen.this, Login.class);
                if (status) {
                    if (getPreferences(MODE_PRIVATE).getBoolean("FIRST_TIME", true)) {
//                        Toast.makeText(SplashScreen.this, "Loading Data...", Toast.LENGTH_SHORT).show();
                        loadNewRides(SplashScreen.this);
                        getPreferences(MODE_PRIVATE).edit().putBoolean("FIRST_TIME", false).commit();
                    }

                } else {
                    Toast.makeText(SplashScreen.this, "No Internet Access", Toast.LENGTH_SHORT).show();
                    getPreferences(MODE_PRIVATE).edit().putBoolean("FIRST_TIME", false).commit();

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            startActivity(intent);
//                            finish();
//                        }
//                    }, 3000);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        });
    }

    private void setTimerForRefreshRideService() {
        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                InternetConnectionChecker.isConnectedToInternet(SplashScreen.this, new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            Intent intent = new Intent(SplashScreen.this, RefreshRideService.class);
                            startService(intent);

                        }
                    }
                });
            }
        };

        mTimer.scheduleAtFixedRate(mTimerTask, new Date(System.currentTimeMillis() + INITIAL_SERVICE_DELAY), INITIAL_SERVICE_DELAY);
        //timer.schedule(mTimerTask, new Date(System.currentTimeMillis() + INITIAL_SERVICE_DELAY));


    }


    public void loadNewRides(Context context){
        MainUserFunctions.get_a_rides(context, new OnRidesListListener() {
            @Override
            public void onRidesRefresh(ArrayList<Ride> newItems) {
                for(Ride ride:newItems)
                    try {
                        RideDAO.addNewRide(ride);

                    } catch (EmptyFieldException e) {
                        e.printStackTrace();
                    } catch (InvalidInputException e) {
                        e.printStackTrace();
                    }

            }
        });

    }

}
