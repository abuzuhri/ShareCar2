package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.plus.Plus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.Login;
import sharearide.com.orchidatech.jma.sharearide.Activity.ShareRide;
import sharearide.com.orchidatech.jma.sharearide.Activity.UserProfile;
import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.ChatDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnAddressFetched;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnFetchMyRides;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInboxFetchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSendPasswordListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnUpdateRideListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 10/9/2015.
 */
public class MainUserFunctions {
    private static String PREFS_NAME = "";
    private MainUserFunctions(){}
    final static int MAX_NUM_RIDES = 200;

    public static void login(final Context context, final String email, final String password, final ProgressDialog mProgressDialog){

        ///on success
//        if(mProgressDialog.isShowing())
//            mProgressDialog.dismiss();

        // on fail

//        if(mProgressDialog.isShowing())
//            mProgressDialog.dismiss();
        ////
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
//        Log.i("Login ", "Processing");
        mProgressDialog.show();
        UserOperations.getInstance(context).login(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("login");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long id = mJsonObject.getLong("id");
//                            Log.i("Success", id + "");

                        String username = mJsonObject.getString("username");
                        String password = mJsonObject.getString("password");
                        String image = mJsonObject.getString("img");
                        String address = mJsonObject.getString("address");
                        long birthdate = mJsonObject.getLong("birthdate");
                        String gender = mJsonObject.getString("Gender");
                        String phone = mJsonObject.getString("phone");
                        String email = mJsonObject.getString("email");

                        // Store in DB
                        UserDAO.addNewUser(id, username, password, image, address, birthdate, gender, phone, email);

                        //UserDAO.addNewUser();


                        context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit().putLong("id", id).commit();
                        context.startActivity(new Intent(context, ShareRide.class));

                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {

                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFail(String error) {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void find_all_ride(final OnSearchListener listener, final Context context, final String item, final long user_id, final int offset, long last_id_server ){
        Map<String, String> params = new HashMap<>();


        params.put("item", item);
        params.put("user_id", user_id + "");
        params.put("offset", String.valueOf(offset));
        params.put("last_id", String.valueOf(last_id_server));
        final ArrayList<Ride> allMatchedRides = new ArrayList<Ride>();
        final  Map<Ride, User> matchedRidesData = new HashMap<Ride, User>();
        UserOperations.getInstance(context).getSearchAllResult(params, new OnLoadFinished() {
            JSONArray mJsonArray;

            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");


                    if (success) {
                        mJsonArray = jsonObject.getJSONArray("rides");

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
                            String price = mJsonObject.getString("price");
                            String more_info = mJsonObject.getString("more_info");
                            double from_Lattitude = Double.parseDouble(mJsonObject.getString("from_latitude"));
                            double from_Longitude = Double.parseDouble(mJsonObject.getString("from_longitude"));
                            double to_latitude = Double.parseDouble(mJsonObject.getString("to_latitude"));
                            double to_longitude = Double.parseDouble(mJsonObject.getString("to_longitude"));
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to,
                                    date_time, price, more_info, from_Lattitude, from_Longitude, to_latitude, to_longitude);
                            allMatchedRides.add(ride);
                            JSONObject userJsonObject = mJsonObject.getJSONObject("user");
                            User user = new User(ride.getUserId(), null, userJsonObject.getString("username"),
                                    null, userJsonObject.getString("img"), userJsonObject.getString("phone"), userJsonObject.getString("email"), null, 1, userJsonObject.getString("Gender"));
                            matchedRidesData.put(ride, user);

//                            Toast.makeText(context, matchedRidesData.size() + ", "  + allMatchedRides.size(), Toast.LENGTH_LONG).show();

                        }
                        int count = jsonObject.getInt("count");
                        long last_id = Long.parseLong(jsonObject.getString("last_id"));
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData, count, last_id);

                   /* Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                    RideDAO.addNewRide(ride);*/
                        //  Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData, -1, -1);

                    }


                } catch (JSONException e) {
                    Toast.makeText(context,"An error occurred, try again", Toast.LENGTH_LONG).show();

                    listener.onSearchFailed("An error occurred, try again");


                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                listener.onSearchFailed(error);

            }
        });
    }
    public static void signUp(final Context context, final String username, final String password, final String image, final String address,
                              final String birthdate, final String gender, final String phone, final String email, final OnRequestListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("img", image);
        params.put("address", address);
        params.put("birthdate", birthdate);
        params.put("Gender", gender);
        params.put("phone", phone);
        params.put("email", email);

        UserOperations.getInstance(context).signUp(UrlConstant.SIGNUP_URL, params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("signup");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long user_id = Long.parseLong(mJsonObject.getString("id"));
                        context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit().putLong("id", user_id).commit();
                        UserDAO.addNewUser(user_id, username, password, image, address, Long.parseLong(birthdate), gender, phone, email);
                        Toast.makeText(context, "Registered Succeeded, Enjoy :) ", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, ShareRide.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        listener.onFinished();

                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        listener.onFinished();


                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_SHORT).show();
                    listener.onFinished();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                listener.onFinished();

            }
        });
    }


    public  static void get_a_rides(final Context context, final OnRidesListListener listener) {
        final ArrayList<Ride> newItems = new ArrayList<Ride>(); // list of new items...
        UserOperations.getInstance(context).getAllRides(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("rides");
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
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));
                            String price = mJsonObject.getString("price");
                            String more_info = mJsonObject.getString("more_info");
                            double from_Lattitude = Double.parseDouble(mJsonObject.getString("from_latitude"));
                            double from_Longitude = Double.parseDouble(mJsonObject.getString("from_longitude"));
                            double to_latitude = Double.parseDouble(mJsonObject.getString("to_latitude"));
                            double to_longitude = Double.parseDouble(mJsonObject.getString("to_longitude"));
                            // Store in DB
              /*          Ride ride = null;

                        ride.remoteId = id;
                        ride.userId = user_id;
                        ride.fromCity = city_from;
                        ride.fromState = state_from;
                        ride.fromCountry = country_from;
                        ride.toCity = city_to;
                        ride.toState = state_to;
                        ride.toCountry = country_to;
                        ride.dateTime = date_time;
                        ride.cost = price;*/
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to,
                                    date_time, price, more_info, from_Lattitude, from_Longitude, to_latitude, to_longitude);
                            RideDAO.addNewRide(remoteId, user_id, city_from, city_to, country_from, country_to, state_from, state_to,
                                    date_time, price, more_info,from_Longitude, to_longitude, from_Lattitude, to_latitude);                            newItems.add(ride);
                        }
                        ///To Ensure that max num of items in db is MAX_NUM_RIDES....
                        final ArrayList<Ride> allStoredRides = new ArrayList<>(RideDAO.getAllRides());
                        int numOfRemovedRides = allStoredRides.size() - MAX_NUM_RIDES;
                        for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                            RideDAO.deleteRide(allStoredRides.get(delRideIndex).getRemoteId());
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    listener.onRidesRefresh(newItems);/// refresh listview
                } catch (JSONException e) {
                    listener.onRidesRefreshFailed("An error occurred, try again");
                }
            }

            @Override
            public void onFail(String error) {
                listener.onRidesRefreshFailed(error);
            }
        });

    }



    public static void getAllMessages(final Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("messages");
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                            long id = Long.parseLong(mJsonObject.getString("id"));
                            long sender_id = Long.parseLong(mJsonObject.getString("sender_id"));
                            long receiver_id = Long.parseLong(mJsonObject.getString("receiver_id"));
                            String message = mJsonObject.getString("message");
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));

                            //store in DB
                            ChatDAO.addNewChat(id, message, sender_id, receiver_id, date_time);
                        }
                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();

            }
        });
    }


    public static void getAbout(final Context context) {
        UserOperations.getInstance(context).getAbout(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("about");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    long id = Long.parseLong(mJsonObject.getString("id"));
                    String name = mJsonObject.getString("name");
                    String country = mJsonObject.getString("country");
                    String city = mJsonObject.getString("city");
                    String address = mJsonObject.getString("address");
                    String tel = mJsonObject.getString("tel");
                    String fax = mJsonObject.getString("fax");
                    String facebook_account = mJsonObject.getString("facebook_account");
                    String twitter_account = mJsonObject.getString("twitter_account");
                    String google_account = mJsonObject.getString("google_account");

                    // Store in SharedPreferences
                    SharedPreferences sharedpreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putLong("id", id);
                    editor.putString("name", name);
                    editor.putString("country", country);
                    editor.putString("city", city);
                    editor.putString("address", address);
                    editor.putString("tel", tel);
                    editor.putString("fax", fax);
                    editor.putString("facebook_account", facebook_account);
                    editor.putString("twitter_account", twitter_account);
                    editor.putString("google_account", google_account);

                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }




    public static void find_a_ride(final OnSearchListener listener, final Context context, final String city_from, final String city_to, final String state_from,
                                   final String state_to, final String country_from,
                                   final String country_to, final long user_id, final int offset, long last_id_server){
        final Map<String, String> params = new HashMap<>();
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("user_id", String.valueOf(user_id));
        params.put("offset", String.valueOf(offset));
        params.put("last_id", String.valueOf(last_id_server));
        final ArrayList<Ride> allMatchedRides = new ArrayList<Ride>();
        final  Map<Ride, User> matchedRidesData = new HashMap<Ride, User>();
        UserOperations.getInstance(context).getSearchResult(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                JSONArray mJsonArray;
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        mJsonArray = jsonObject.getJSONArray("rides");
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
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));
                            String price = mJsonObject.getString("price");
                            String more_info = mJsonObject.getString("more_info");
                            double from_Lattitude = Double.parseDouble(mJsonObject.getString("from_latitude"));
                            double from_Longitude = Double.parseDouble(mJsonObject.getString("from_longitude"));
                            double to_latitude = Double.parseDouble(mJsonObject.getString("to_latitude"));
                            double to_longitude = Double.parseDouble(mJsonObject.getString("to_longitude"));
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to,
                                    date_time, price, more_info, from_Lattitude, from_Longitude, to_latitude, to_longitude);
                            allMatchedRides.add(ride);
                            JSONObject userJsonObject = mJsonObject.getJSONObject("user");
                            User user = new User(ride.getUserId(), null, userJsonObject.getString("username"), null, userJsonObject.getString("img"), userJsonObject.getString("phone"), userJsonObject.getString("email"), null, -1, userJsonObject.getString("Gender"));
                            //UserDAO.addNewUser(receiver_id, receiver_username, null, receiver_image, null, 1, null, receiver_phone, receiver_email);

                            UserDAO.addNewUser(ride.getUserId(), userJsonObject.getString("username"), null, userJsonObject.getString("img"), null, 1, null, userJsonObject.getString("phone"),  userJsonObject.getString("email"));
                            RideDAO.addNewRide(remoteId, user_id, city_from, city_to, country_from, country_to, state_from, state_to,
                                    date_time, price, more_info,from_Longitude, to_longitude, from_Lattitude, to_latitude);
                            matchedRidesData.put(ride, user);

//                            Toast.makeText(context, matchedRidesData.size() + ", "  + allMatchedRides.size(), Toast.LENGTH_LONG).show();

                        }
                        int count = jsonObject.getInt("count");
                        long last_id = Long.parseLong(jsonObject.getString("last_id"));
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData, count, last_id);
                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData, -1, -1);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_LONG).show();
                    listener.onSearchFailed(e.getMessage());
                }
                //listener.onSearchSucceed(matchedRidesData);


            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                listener.onSearchFailed(error);
            }
        });
    }

    public static void offerRide(final Context context, final long user_id,
                                 final String city_from, final String city_to,
                                 final String state_from, final String state_to,
                                 final String country_from, final String country_to,
                                 final long date_time, final String price, final String more_info,
                                 final String from_latitude,final String from_longitude,
                                 final String to_latitude,final String to_longitude,final OnRequestFinished listener){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("date_time", String.valueOf(date_time));
        params.put("price", price);
        params.put("from_latitude",from_latitude);
        params.put("from_longitude",from_longitude);
        params.put("to_latitude",to_latitude);
        params.put("to_longitude",to_longitude);
        params.put("more_info", more_info);
        //   params.put("",more_info);

        UserOperations.getInstance(context).offer_a_ride(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        listener.onFinished("success");

                        JSONArray mJsonArray = jsonObject.getJSONArray("ride");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long remoteId = Long.parseLong(mJsonObject.getString("id"));

                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        listener.onFinished("success");

                    }


                } catch (JSONException e) {
                    listener.onFinished("success");
                    e.printStackTrace();

                }
            }

            @Override
            public void onFail(String error) {
                listener.onFinished("success");

                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void socialSignUp(final Context context, final SocialUser user) {
        Map<String,String> params = new HashMap<>();
        params.put("username", user.getName());
        params.put("password", "");
        params.put("img", user.getAvatarURL());
        params.put("address", "1");
        params.put("birthdate", "1");
        params.put("Gender", "");
        params.put("phone", "");
        params.put("email", user.getEmail());
        params.put("social_id", user.getSocial_id());
      //  Toast.makeText(context, user.getSocial_id() + "", Toast.LENGTH_LONG).show();

        UserOperations.getInstance(context).signUp(UrlConstant.SOCIAL_SIGNUP, params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
        //            Toast.makeText(context, success + "", Toast.LENGTH_LONG).show();

                    if (success) {

                        JSONArray mJsonArray = jsonObject.getJSONArray("signup");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        final long user_id = Long.parseLong(mJsonObject.getString("id"));
                        user.setId(user_id + "");
//
//                        Toast.makeText(context, user_id + "", Toast.LENGTH_LONG).show();
                        UserDAO.addNewSocialUser(user);
                        context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit().putLong("id", user_id).commit();
                        context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit().putInt("network", user.getNetwork()).commit();

                        Intent intent;
                        User user = UserDAO.getUserById(user_id);
                        if(TextUtils.isEmpty(user.getEmail()) || TextUtils.isEmpty(user.getPhone()))
                            intent = new Intent(context, UserProfile.class);
                        else
                            intent = new Intent(context, ShareRide.class);


                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }


                } catch (JSONException e) {
                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_SHORT).show();
                    context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("id").commit();
                    //  getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();
                    int i = context.getSharedPreferences("pref", context.MODE_PRIVATE).getInt("network", -1);
                    if (i == 1) {
                        if (Login.mGoogleApiClient.isConnected()) {
                            Plus.AccountApi.clearDefaultAccount(Login.mGoogleApiClient);
                            Login.mGoogleApiClient.disconnect();
                            Login.mGoogleApiClient.connect();
                            context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("network").commit();

                        }
                        System.exit(1);
                    }    if (i == 2) {
                        LoginManager.getInstance().logOut();
                        context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("network").commit();
                        System.exit(1);
                    }
//                    Toast.makeText(context, "Can not connect server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("id").commit();
                //  getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();
                int i = context.getSharedPreferences("pref", context.MODE_PRIVATE).getInt("network", -1);
                if (i == 1) {
                    if (Login.mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(Login.mGoogleApiClient);
                        Login.mGoogleApiClient.disconnect();
                        Login.mGoogleApiClient.connect();
                        context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("network").commit();

                    }
                    System.exit(1);
                }    if (i == 2) {
                    LoginManager.getInstance().logOut();
                    context.getSharedPreferences("pref", context.MODE_PRIVATE).edit().remove("network").commit();
                    System.exit(1);
                }
            }
        });
    }


    public static void getInbox(final Context context, final OnInboxFetchListener listener, final long user_id) {

        final Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id+"");

        final Map<Chat, ArrayList<User>> messages_data = new HashMap<>();
        final ArrayList<Chat> all_messages = new ArrayList<>();
        final ArrayList<Long> users_id = new ArrayList<>();
        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {

                        JSONArray mJsonArray = jsonObject.getJSONArray("messages");

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                            long id = Long.parseLong(mJsonObject.getString("id"));
                            long sender_id = Long.parseLong(mJsonObject.getString("sender_id"));
                            long receiver_id = Long.parseLong(mJsonObject.getString("receiver_id"));
                            String message = mJsonObject.getString("message");
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));

                            if (sender_id != user_id && users_id.contains(sender_id))
                                continue;
                            else if (receiver_id != user_id && users_id.contains(receiver_id))
                                continue;
                            else if (sender_id != user_id)
                                users_id.add(sender_id);
                            else
                                users_id.add(receiver_id);

                            //   ChatDAO.addNewChat(id, message, sender_id, receiver_id, date_time);
                            Chat chat = new Chat();
                            chat.setRemoteId(id);
                            chat.setSenderId(sender_id);
                            chat.setReceiverId(receiver_id);
                            chat.setDateTime(date_time);
                            chat.setMessage(message);

                            JSONObject senderJsonObject = mJsonObject.getJSONObject("sender");
                            String sender_username = senderJsonObject.getString("username");
                            String sender_email = senderJsonObject.getString("email");
                            String sender_phone = senderJsonObject.getString("phone");
                            String sender_image = senderJsonObject.getString("img");
                            //  Long sender_birthdate = Long.parseLong(senderJsonObject.getString("birthdate"));
                            String sender_gender = senderJsonObject.getString("Gender");

                            JSONObject receiverJsonObject = mJsonObject.getJSONObject("receiver");
                            String receiver_username = receiverJsonObject.getString("username");
                            String receiver_email = receiverJsonObject.getString("email");
                            String receiver_phone = receiverJsonObject.getString("phone");
                            String receiver_image = receiverJsonObject.getString("img");
                            //  Long receiver_birthdate = Long.parseLong(receiverJsonObject.getString("birthdate"));
                            String receiver_gender = receiverJsonObject.getString("Gender");

                            User sender_info = new User();
                            sender_info.setRemoteId(sender_id);
                            sender_info.setUsername(sender_username);
                            sender_info.setEmail(sender_email);
                            sender_info.setPhone(sender_phone);
                            sender_info.setImage(sender_image);
                            sender_info.setBirthdate(1);
                            sender_info.setGender(sender_gender);

                            User receiver_info = new User();
                            receiver_info.setRemoteId(receiver_id);
                            receiver_info.setUsername(receiver_username);
                            receiver_info.setEmail(receiver_email);
                            receiver_info.setPhone(receiver_phone);
                            receiver_info.setImage(receiver_image);
                            receiver_info.setBirthdate(1);
                            receiver_info.setGender(receiver_gender);


                            ArrayList<User> persons = new ArrayList<User>();
                            persons.add(sender_info);
                            persons.add(receiver_info);

//                            UserDAO.addNewUser(sender_info);
//                            UserDAO.addNewUser(receiver_info);

                            UserDAO.addNewUser(receiver_id, receiver_username, null, receiver_image, null, 1, null, receiver_phone, receiver_email);
                            UserDAO.addNewUser(sender_id, sender_username, null, sender_image, null, 1, null, sender_phone, sender_email);
                            ChatDAO.addNewChat(chat);
                            messages_data.put(chat, persons);
                            all_messages.add(chat);
                        }
                        listener.onFetchInboxSucceed(all_messages, messages_data);

                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        listener.onFetchInboxFailed(message);

                    }
                } catch (JSONException e) {

                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_LONG).show();
                    listener.onFetchInboxFailed("An error occurred, try again");
                }

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                listener.onFetchInboxFailed(error);

            }
        });
    }
    public static void add_message(final Context context, final String message, final long sender_id, final long receiver_id, final long date_time) {
        final Map<String, String> params = new HashMap<>();
        params.put("message", message);
        params.put("sender_id", sender_id+"");
        params.put("receiver_id", receiver_id+"");
        params.put("date_time", date_time+"");
        UserOperations.getInstance(context).addMessage(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");

                } catch (JSONException e) {
//                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
//                Toast.makeText(context, error, Toast.LENGTH_LONG).show();

            }
        });

    }
    public static void forgetPassword(final Context context, final String email, final OnSendPasswordListener listener){
        final Map<String, String> params = new HashMap<>();
        params.put("email", email);
        UserOperations.getInstance(context).forgetPassword(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    String password = jsonObject.getString("password");
                    listener.onSendingSuccess(password);
                }
                else {
                    String message = jsonObject.getString("message");
                    listener.onSendingFails(message);
                }
            }

            @Override
            public void onFail(String error) {
                listener.onSendingFails(error);
            }
        });
    }
    public static void get_address(final Context context, final OnAddressFetched listener, String longitude, String lattitude, String format){
        final Map<String, String> params = new HashMap<>();
        params.put("lat", lattitude);
        params.put("lon", longitude);
        params.put("format", format);
        UserOperations.getInstance(context).getAddress(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                try {
                    JSONObject address = jsonObject.getJSONObject("address");
                    String country = address.optString("country");
                    String state = address.optString("state");
                    String city = address.optString("city");
                    String region = address.optString("region");
                    String village = address.optString("village");
                    String road = address.optString("road");
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("country", country);
                    data.put("state", state);
                    data.put("city", city);
                    data.put("region", region);
                    data.put("village", village);
                    data.put("road", road);
                    listener.onFetched(data);
                }catch (JSONException e){
//                    Toast.makeText(context, "An error occurred, try again", Toast.LENGTH_LONG).show();
                    listener.onFailed( "An error occurred, try again");


                }

            }

            @Override
            public void onFail(String error) {
                listener.onFailed(error);
            }
        });

    }

    public static void update_ride(final Context context, final long ride_id,
                                   final String city_from, final String city_to,
                                   final String state_from, final String state_to,
                                   final String country_from, final String country_to,
                                   final long date_time, final String price,
                                   final String from_latitude,final String from_longitude,
                                   final String to_latitude,final String to_longitude, String more_info,final OnUpdateRideListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("ride_id", String.valueOf(ride_id));
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("date_time", String.valueOf(date_time));
        params.put("price", price);
        params.put("from_latitude",from_latitude);
        params.put("from_longitude",from_longitude);
        params.put("to_latitude",to_latitude);
        params.put("to_longitude",to_longitude);
        params.put("more_info",more_info);
        UserOperations.getInstance(context).updateRide(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(context, "Updated Successfully...", Toast.LENGTH_LONG).show();
                        JSONArray mJsonArray = jsonObject.getJSONArray("ride");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long remoteId = Long.parseLong(mJsonObject.getString("id"));
                        long user_id = Long.parseLong(mJsonObject.getString("user_id"));
                        String city_from = mJsonObject.getString("city_from");
                        String city_to = mJsonObject.getString("city_to");
                        String state_from = mJsonObject.getString("state_from");
                        String state_to = mJsonObject.getString("state_to");
                        String country_from = mJsonObject.getString("country_from");
                        String country_to = mJsonObject.getString("country_to");
                        long date_time = Long.parseLong(mJsonObject.getString("date_time"));
                        String price = mJsonObject.getString("price");
                        double from_Lattitude = Double.parseDouble(mJsonObject.getString("from_latitude"));
                        double from_Longitude = Double.parseDouble(mJsonObject.getString("from_longitude"));
                        double to_latitude = Double.parseDouble(mJsonObject.getString("to_latitude"));
                        double to_longitude = Double.parseDouble(mJsonObject.getString("to_longitude"));
                        String more_info = mJsonObject.getString("more_info");
                        Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to,
                                date_time, price, more_info, from_Lattitude, from_Longitude, to_latitude, to_longitude);
                        RideDAO.addNewRide(remoteId, user_id, city_from, city_to, country_from, country_to, state_from, state_to,
                                date_time, price, more_info, from_Longitude, to_longitude, from_Lattitude, to_latitude);
                        listener.onFinished(ride);

                    } else {
                        Toast.makeText(context, "Updating Failed...", Toast.LENGTH_LONG).show();
                        listener.onFailed("Updating Failed...");

                    }
                } catch (JSONException e) {
                    Toast.makeText(context,  "An error occurred, try again", Toast.LENGTH_LONG).show();
                    listener.onFailed("An error occurred, try again");

                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                listener.onFailed(error);

            }
        });
    }

    public static void deleteRide(final Context context, final long ride_id, final OnRequestFinished listener){
        Map<String, String> params = new HashMap<>();
        params.put("ride_id", String.valueOf(ride_id));
        UserOperations.getInstance(context).deleteRide(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    //           RideDAO.deleteRide(ride_id);
                    listener.onFinished("Deleted Successfully...");
                } else {
                    listener.onFinished("Deleting Failed...");
                }

            }

            @Override
            public void onFail(String error) {
                listener.onFinished(error);

            }
        });

    }

    public static void get_my_rides(final Context context, final OnFetchMyRides listener, long user_id,final int offset, long last_id_server){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("offset", String.valueOf(offset));
        params.put("last_id", String.valueOf(last_id_server));
        final ArrayList<Ride> all_my_rides = new ArrayList<>();

        UserOperations.getInstance(context).get_my_rides(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                try{
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {

                        JSONArray mJsonArray = jsonObject.getJSONArray("rides");

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
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));
                            String price = mJsonObject.getString("price");
                            double from_Lattitude = Double.parseDouble(mJsonObject.getString("from_latitude"));
                            double from_Longitude = Double.parseDouble(mJsonObject.getString("from_longitude"));
                            double to_latitude = Double.parseDouble(mJsonObject.getString("to_latitude"));
                            double to_longitude = Double.parseDouble(mJsonObject.getString("to_longitude"));
                            String more_info = mJsonObject.getString("more_info");
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to,
                                    date_time, price, more_info, from_Lattitude, from_Longitude, to_latitude, to_longitude);

                            RideDAO.addNewRide(remoteId, user_id, city_from, city_to, country_from, country_to, state_from, state_to,
                                    date_time, price, more_info, from_Longitude, to_longitude, from_Lattitude, to_latitude);

                            all_my_rides.add(ride);
                        }
                        // Toast.makeText(context, all_my_rides.size()+"", Toast.LENGTH_LONG).show();
                        int count = jsonObject.getInt("count");
                        long last_id = Long.parseLong(jsonObject.getString("last_id"));
                        listener.onFetched(all_my_rides, count, last_id);
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        listener.onFetched(all_my_rides, -1, -1);
                    }
                } catch (JSONException e) {
                    listener.onFailed("An error occurred, try again");

                }
            }

            @Override
            public void onFail(String error) {
                listener.onFailed(error);
            }
        });

    }

    public static void uploadImage(final Context context, final long user_id, final String image_str, final OnRequestFinished listener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("image", image_str);
        UserOperations.getInstance(context).uploadImage(params, new OnLoadFinished(){
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                boolean success = jsonObject.getBoolean("success");
                if(success){
                    String imageUrl =jsonObject.getString("image_url");
                    listener.onFinished(imageUrl);
                }else{
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    listener.onFailed();
                }

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG) .show();
                listener.onFailed();
            }
        });

    }
    public static void updateProfile(final Context context, final long user_id, final String email, final String phone, String password, final OnRequestFinished listener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("phone", phone);
        params.put("email", email);
        params.put("password", password);
        UserOperations.getInstance(context).updateProfile(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    listener.onFinished(jsonObject.getString("message"));
                } else {
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    listener.onFailed();
                }

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                listener.onFailed();
            }
        });
    }
    public static void get_last_record(final Context context, final long user_id, final OnLoadFinished loadFinished) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        UserOperations.getInstance(context).getLastRecord(params, loadFinished);

    }
}
