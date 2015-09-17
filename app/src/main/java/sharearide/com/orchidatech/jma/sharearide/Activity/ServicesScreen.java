package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.FacebookLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.GooglePlusLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Services.RefreshRideService;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnChattingListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoginListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;


public class ServicesScreen extends AppCompatActivity implements View.OnClickListener {

    Button login, findRide, offerRide;
    private static final long INITIAL_SERVICE_DELAY = 60 * 1000L;//FOR ONE MINUTE
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_services);

        /*
        login = (Button) findViewById(R.id.button_login_main);
        login.setOnClickListener(this);

        findRide = (Button) findViewById(R.id.button_findRide_main);
        findRide.setOnClickListener(this);

        offerRide = (Button) findViewById(R.id.button_offerRide_main);
        offerRide.setOnClickListener(this);
        */

/*        InternetConnectionChecker.isConnectedToInternet(new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if(status)
                    Toast.makeText(ServicesScreen.this, "Internet Access", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ServicesScreen.this, "No Internet Access", Toast.LENGTH_LONG).show();
            }
        });*/


           setTimerForRefreshRideService();
           /* signupTest();
            loginTest();*/
    }

    private void signupTest() {
        Calendar c = Calendar.getInstance();
        c.set(1991, 7, 28);
        MainUserFunctions.signUp(this, "Ali", "123", "Ali.jpg", "Gaza strip-Rafah", c.getTimeInMillis()+"", "Male", "0597244687", "bmmsh18_pal@hotmail.com");
    }

    private void loginTest() {
        MainUserFunctions.login(this, "Ali", "123");
    }

    private void setTimerForRefreshRideService() {
        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
               InternetConnectionChecker.isConnectedToInternet(new OnInternetConnectionListener() {
                   @Override
                   public void internetConnectionStatus(boolean status) {
                       if (status) {
                          startService(new Intent(ServicesScreen.this, RefreshRideService.class));
                       }else if(getPreferences(MODE_PRIVATE).getBoolean("FIRST_TIME", true)) {
                           Toast.makeText(ServicesScreen.this, "No Internet Access", Toast.LENGTH_LONG).show();
                           getPreferences(MODE_PRIVATE).edit().putBoolean("FIRST_TIME", false).commit();
                       }
                   }
               });
            }
        };

        mTimer.scheduleAtFixedRate(mTimerTask , new Date(System.currentTimeMillis()), INITIAL_SERVICE_DELAY);

       // timer.schedule(mTimerTask, new Date(System.currentTimeMillis() + INITIAL_SERVICE_DELAY));


    }

    @Override
    public void onClick(View v) {

        /*
        if (v.getId() == R.id.button_login_main) {
            startActivity(new Intent(this, Login.class));

        } else if (v.getId() == R.id.button_findRide_main) {
            startActivity(new Intent(this, FindRide.class));

        } else if (v.getId() == R.id.button_offerRide_main) {
            startActivity(new Intent(this, OfferRide.class));
        }
        */
    }

    public void facebookLogin() {
        FacebookLogin facebookLogin = new FacebookLogin(this);
        boolean isLoggedIn = facebookLogin.isLoggedIn();
        if (!isLoggedIn) {
            facebookLogin.Login(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser socialUser) {
                    UserDAO.addNewSocialUser(socialUser);
///                    MainUserFunctions.signUp(getApplicationContext(), socialUser.getName(), null, socialUser.getAvatarURL(), null, null, null, null, socialUser.getEmail());

                }

                @Override
                public void onFail() {

                }
            });
        }
    }
    public void googleLogin() {
        GooglePlusLogin googlePlusLogin = new GooglePlusLogin(this);
        boolean isLoggedIn = googlePlusLogin.isLoggedIn();
        if (!isLoggedIn) {
            googlePlusLogin.Login(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser socialUser) {
                    UserDAO.addNewSocialUser(socialUser);
                    //  MainUserFunctions.signUp(getApplicationContext(), socialUser.getName(), null, socialUser.getAvatarURL(), null, null, null, null, socialUser.getEmail());
                }

                @Override
                public void onFail() {

                }
            });
        }
    }

    public void find_a_ride(final OnSearchListener listener, final Context context, final String item){
        InternetConnectionChecker.isConnectedToInternet(new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if(status)
                    MainUserFunctions.find_a_ride(listener, context, item);
                else
                    Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
            }
        });;
    }

    /////Called when last item in listview accessed....
    public void loadNewData(final OnRidesListListener listener){
        final int MAX_NUM_RIDES = 200;

       //view loading progress...

         ///load data from server DB...
        UserOperations.getInstance(getApplicationContext()).getAllRides(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        long remoteId = Long.parseLong(mJsonObject.getString("id"));
                        long user_id = Long.parseLong(mJsonObject.getString("user_id"));
                        String city_from = mJsonObject.getString("city_from");
                        String city_to = mJsonObject.getString("city_to");
                        String state_from = mJsonObject.getString("state_from");
                        String state_to = mJsonObject.getString("state_to");
                        String country_from = mJsonObject.getString("country_from");
                        String country_to = mJsonObject.getString("country_to");
                        long date_time = mJsonObject.getLong("date_time");
                        double price = Double.parseDouble(mJsonObject.getString("price"));
                        Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                        RideDAO.addNewRide(ride);
                    }

                    ///To Ensure that max num of items in db is MAX_NUM_RIDES....
                    final ArrayList<Ride> allStoredRides = new ArrayList<>(RideDAO.getAllRides());
                    int numOfRemovedRides = allStoredRides.size() - MAX_NUM_RIDES;
                    for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                        RideDAO.deleteRide(allStoredRides.get(delRideIndex).getId());

                   ArrayList<Ride> adapterList = new ArrayList<Ride>(); // list of items used by listview adapter

                    //To add all new items in DB to listview
                    for(Ride storedRide : RideDAO.getAllRides()){
                        boolean isFoundInListView = false;
                        for(Ride listRide : adapterList){
                            if(storedRide.getRemoteId() == listRide.getRemoteId()){
                               isFoundInListView = true;
                                break;
                            }
                        }
                        if(!isFoundInListView)
                            adapterList.add(storedRide);
                    }
                    /// To synchronize listview with local DB, remove from list if it's size exceeded MAX_NUM_RIDES
                // adapterList = (adapterList.size()> MAX_NUM_RIDES)?(ArrayList<Ride>) adapterList.subList(adapterList.size()-MAX_NUM_RIDES, adapterList.size()): adapterList;

                        //hide loading progress....
                    listener.onRidesRefresh();/// refresh listview
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                //hide loading progress....
            }
        });

    }
    public void offerRide(final Context context,  final long user_id,  final String city_from,  final String city_to,  final String state_from,  final String state_to,  final String country_from,  final String country_to,  final long date_time,  final double price)
        {
            InternetConnectionChecker.isConnectedToInternet(new OnInternetConnectionListener() {
                @Override
                public void internetConnectionStatus(boolean status) {
                    if (status)
                         MainUserFunctions.offerRide(context, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                    else
                        Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
                }
            });
        }

    public void last_chatting_users(final Context context, final OnChattingListListener listener, final long id){
        InternetConnectionChecker.isConnectedToInternet(new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
                    User user = UserDAO.getUserById(id);
                    String username = user.getUsername();
                    String password = user.getPassword();
                   if(username != null && password != null)
                        MainUserFunctions.last_chatting_users(context, listener, id, username, password);
                }
                else
                    Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
     /*  if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
            Log.i("RefreshRideService", "Timer Stopped!!");
        }*/

    }

}
