package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.Login;
import sharearide.com.orchidatech.jma.sharearide.Activity.ShareRide;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.ChatDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.CountryDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInboxFetchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSendPasswordListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 10/9/2015.
 */
public class MainUserFunctions {
    private static String PREFS_NAME = "";
    private MainUserFunctions(){}
    final static int MAX_NUM_RIDES = 200;

    public static void login(final Context context, final String username, final String password){


        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
//        Log.i("Login ", "Processing");

        UserOperations.getInstance(context).login(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("login");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long id = mJsonObject.getLong("id");
//                            Log.i("Success", id + "");
                        if(UserDAO.getUserByUserName(username,password)==null){
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



                        }

                        context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit().putLong("id", id).commit();
                        context.startActivity(new Intent(context, ShareRide.class));

                    }else{
                        Toast.makeText(context  , jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void find_all_ride(final OnSearchListener listener, final Context context, final String item, final long user_id ){
        Map<String, String> params = new HashMap<>();


        params.put("item", item);
        params.put("user_id", user_id+"");
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
                            double price = Double.parseDouble(mJsonObject.getString("price"));
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                            allMatchedRides.add(ride);
                            JSONObject userJsonObject = mJsonObject.getJSONObject("user");
                            User user = new User(ride.getUserId(), null, userJsonObject.getString("username"),
                                    null, userJsonObject.getString("img"), userJsonObject.getString("phone"), userJsonObject.getString("email"), null, 1, userJsonObject.getString("Gender"));
                            matchedRidesData.put(ride, user);

//                            Toast.makeText(context, matchedRidesData.size() + ", "  + allMatchedRides.size(), Toast.LENGTH_LONG).show();

                        }
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData);
                   /* Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                    RideDAO.addNewRide(ride);*/
                      //  Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void signUp(final Context context, final String username, final String password, final String image, final String address, final String birthdate, final String gender, final String phone, final String email) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("img", image);
        params.put("address", address);
        params.put("birthdate", birthdate);
        params.put("Gender", gender);
        params.put("phone", phone);
        params.put("email", email);

        UserOperations.getInstance(context).signUp(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("signup");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long user_id = Long.parseLong(mJsonObject.getString("id"));
                        UserDAO.addNewUser(user_id, username, password, image, address, Long.parseLong(birthdate), gender, phone, email);
                        Toast.makeText(context, "Registered Succeeded, login to continue...", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }, 3000);
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (EmptyFieldException e) {
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
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
                    if (success){
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
                            double price = Double.parseDouble(mJsonObject.getString("price"));

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
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                            RideDAO.addNewRide(ride);
                            newItems.add(ride);
                         }
                        ///To Ensure that max num of items in db is MAX_NUM_RIDES....
                        final ArrayList<Ride> allStoredRides = new ArrayList<>(RideDAO.getAllRides());
                        int numOfRemovedRides = allStoredRides.size() - MAX_NUM_RIDES;
                        for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                            RideDAO.deleteRide(allStoredRides.get(delRideIndex).getId());
                    }else{
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    listener.onRidesRefresh(newItems);/// refresh listview
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.displayMessage();
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.displayMessage();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                listener.onRidesRefreshFailed(error);
            }
        });

    }


    public static void getAllCountries(final Context context) {
        UserOperations.getInstance(context).getAllCountries(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        JSONArray mJsonArray = jsonObject.getJSONArray("countries");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                            long id = Long.parseLong(mJsonObject.getString("id"));
                            String name = mJsonObject.getString("name");
                            String alpha_2 = mJsonObject.getString("alpha_2");
                            String alpha_3 = mJsonObject.getString("alpha_3");

                            // Store in DB
                            CountryDAO.addNewCountry(id, name, alpha_2);
                        }
                    }
                    else{
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.displayMessage();
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.displayMessage();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    /*
        public void getAllApps(final Context context) {
            UserOperations.getInstance(context).getAllApps(new OnLoadFinished() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                        JSONArray mJsonArray = jsonObject.getJSONArray("apps");
                        mJsonArray = jsonObject.getJSONArray("");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                            long id = Long.parseLong(mJsonObject.getString("id"));
                            String name = mJsonObject.getString("name");
                            String description = mJsonObject.getString("description");
                            String apple_link = mJsonObject.getString("app_link");
                            String google_link = mJsonObject.getString("google_link");
    
                            // Store in DB
                        }
                        }else{
                            String message = jsonObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
    
                @Override
                public void onFail(String error) {
                }
            });
        }
    */
    public static void getAllMessages(final Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
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
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.displayMessage();
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.displayMessage();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

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


    public static void userInfo(final Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getUserInfo(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("user");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long id = Long.parseLong(mJsonObject.getString("id"));
                        String username = mJsonObject.getString("username");
                        String password = mJsonObject.getString("password");
                        String image = mJsonObject.getString("img");
                        String address = mJsonObject.getString("address");
                        long birthdate = Long.parseLong(mJsonObject.getString("birthdate"));
                        String gender = mJsonObject.getString("Gender");
                        String phone = mJsonObject.getString("phone");
                        String email = mJsonObject.getString("email");

                        // Store in DB
                        UserDAO.addNewUser(id, username, password, image, address, birthdate, gender, phone, email);
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.displayMessage();
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.displayMessage();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public static  void userName(final Context context, String id) {

        Map<String, String> params = new HashMap<>();
        params.put("username", id);
        UserOperations.getInstance(context).getUserName(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("user");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        String username = mJsonObject.getString("username");

                        //store in DB
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } /*catch (EmptyFieldException e) {
                    e.displayMessage();
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.displayMessage();
                    e.printStackTrace();
                }*/

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void find_a_ride(final OnSearchListener listener, final Context context, final String city_from, final String city_to, final String state_from, final String state_to, final String country_from, final String country_to, final long date_time, final long user_id){
        final Map<String, String> params = new HashMap<>();
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("date_time", String.valueOf(date_time));
        params.put("user_id", String.valueOf(user_id));
        final ArrayList<Ride> allMatchedRides = new ArrayList<Ride>();
        final  Map<Ride, User> matchedRidesData = new HashMap<Ride, User>();
        UserOperations.getInstance(context).getSearchResult(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject){

                JSONArray mJsonArray;
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
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
                            double price = Double.parseDouble(mJsonObject.getString("price"));
                            Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                            allMatchedRides.add(ride);
                            JSONObject userJsonObject = mJsonObject.getJSONObject("user");
                            User user = new User(ride.getUserId(), null, userJsonObject.getString("username"), null, userJsonObject.getString("img"), userJsonObject.getString("phone"), userJsonObject.getString("email"), null, -1, userJsonObject.getString("Gender"));
                            matchedRidesData.put(ride, user);

//                            Toast.makeText(context, matchedRidesData.size() + ", "  + allMatchedRides.size(), Toast.LENGTH_LONG).show();

                        }
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData);
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        listener.onSearchSucceed(allMatchedRides, matchedRidesData);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onSearchFailed(e.getMessage());
                    e.printStackTrace();
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

    public static void offerRide(final Context context, final long user_id, final String city_from, final String city_to, final String state_from, final String state_to, final String country_from, final String country_to, final long date_time, final double price){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("date_time", String.valueOf(date_time));
        params.put("price", String.valueOf(price));
        UserOperations.getInstance(context).offer_a_ride(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        JSONArray mJsonArray = jsonObject.getJSONArray("ride");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        long remoteId = Long.parseLong(mJsonObject.getString("id"));
                   /* Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                    RideDAO.addNewRide(ride);*/
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

                    }else{
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void getInbox(final Context context, final OnInboxFetchListener listener, final long user_id, String username, String password) {

        final Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        final Map<Chat, ArrayList<User>> messages_data = new HashMap<>();
        final ArrayList<Chat> all_messages = new ArrayList<>();
        final ArrayList<Long> users_id = new ArrayList<>();
        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if(success){

                        JSONArray mJsonArray = jsonObject.getJSONArray("messages");
                        Toast.makeText(context, "dfgdg"+mJsonArray.length(), Toast.LENGTH_LONG).show();

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                            long id = Long.parseLong(mJsonObject.getString("id"));
                            long sender_id = Long.parseLong(mJsonObject.getString("sender_id"));
                            long receiver_id = Long.parseLong(mJsonObject.getString("receiver_id"));
                            String message = mJsonObject.getString("message");
                            long date_time = Long.parseLong(mJsonObject.getString("date_time"));

                            if(sender_id != user_id && users_id.contains(sender_id))
                                continue;
                            else if(receiver_id != user_id && users_id.contains(receiver_id))
                                continue;
                            else if(sender_id != user_id)
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
//
//                            if(UserDAO.getUserById(receiver_id) == null)
//                                UserDAO.addNewUser(receiver_id, receiver_username, null, receiver_image, null, receiver_birthdate, receiver_gender, receiver_phone, receiver_email);
//                            if(UserDAO.getUserById(sender_id) == null)
//                                UserDAO.addNewUser(sender_id, sender_username, null, sender_image, null, sender_birthdate, sender_gender, sender_phone, sender_email);

//                            User sender_info = UserDAO.getUserById(sender_id);
//                            User receiver_info = UserDAO.getUserById(receiver_id);

                            User sender_info = new User();
                            sender_info.setRemoteId(sender_id);
                            sender_info.setUsername(sender_username);
                            sender_info.setEmail(sender_email);
                            sender_info.setPhone(sender_phone);
                            sender_info.setImage(sender_image);
                            //sender_info.setBirthdate(sender_birthdate);
                            sender_info.setGender(sender_gender);

                            User receiver_info = new User();
                            receiver_info.setRemoteId(receiver_id);
                            receiver_info.setUsername(receiver_username);
                            receiver_info.setEmail(receiver_email);
                            receiver_info.setPhone(receiver_phone);
                            receiver_info.setImage(receiver_image);
                           // receiver_info.setBirthdate(receiver_birthdate);
                            receiver_info.setGender(receiver_gender);

                            ArrayList<User> persons = new ArrayList<User>();
                            persons.add(sender_info);
                            persons.add(receiver_info);
                            messages_data.put(chat, persons);
                            all_messages.add(chat);
                        }
                        listener.onFetchInboxSucceed(all_messages, messages_data);
//Toast.makeText(context,""+all_messages.size(),Toast.LENGTH_LONG).show();

                    }else{
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        listener.onFetchInboxFailed(message);

                    }
                }catch (JSONException e) {

                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    listener.onFetchInboxFailed(e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
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
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();

            }
        });

    }
    public static void forgetPassword(final Context context, final String email, final OnSendPasswordListener listener){
        final Map<String, String> params = new HashMap<>();
        params.put("email", email);
        UserOperations.getInstance(context).forgetPassword(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                String message = jsonObject.getString("message");
                boolean success = jsonObject.getBoolean("success");
                if(success)
                    listener.onSendingSuccess(message);
                else
                    listener.onSendingFails(message);
            }

            @Override
            public void onFail(String error) {
                listener.onSendingFails(error);
            }
        });
    }
}
