package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.ShareRide;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.ChatDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.CountryDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnChattingListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRidesListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 10/9/2015.
 */
public class MainUserFunctions {
    private static String PREFS_NAME = "";
    private MainUserFunctions(){}
    final static int MAX_NUM_RIDES = 200;

    public static void login(final Context context,String username, String password){


        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        Log.i("Login ", "Processing");

        UserOperations.getInstance(context).login(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    boolean success = mJsonObject.getBoolean("login");
                    Log.i("Success", success+"");
                    if(success){
                       context.startActivity(new Intent(context,ShareRide.class));
                    }
//                        Log.i("Login ", "Success");
                    else
                        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
               }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, "An Error Occurred, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void signUp(final Context context, String username, String password, String image, String address, String birthdate, String gender, String phone, String email) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("img", image);
        params.put("address", address);
        params.put("birthdate", birthdate);
        params.put("Gender", gender);
        params.put("phone", phone);
        params.put("email", email);
        Log.i("Signup ", "Processing");

        UserOperations.getInstance(context).signUp(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        boolean success = mJsonObject.getBoolean("signup");
                        if(success) {
                            Toast.makeText(context, "Registered Succeeded, Forwarding to Share A Ride...", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, ShareRide.class);
                                context.startActivity(intent);
                            }
                        },2000);
                        }
                            else {
                            Log.i("Signup ", "Failed");

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, "An Error Occurred, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  static void get_a_rides(Context context, final OnRidesListListener listener) {
        final ArrayList<Ride> newItems = new ArrayList<Ride>(); // list of new items...
        UserOperations.getInstance(context).getAllRides(new OnLoadFinished() {
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

                        RideDAO.addNewRide(ride);
                        newItems.add(ride);
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
                ///To Ensure that max num of items in db is MAX_NUM_RIDES....
                final ArrayList<Ride> allStoredRides = new ArrayList<>(RideDAO.getAllRides());
                int numOfRemovedRides = allStoredRides.size() - MAX_NUM_RIDES;
                for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                    RideDAO.deleteRide(allStoredRides.get(delRideIndex).getId());
                listener.onRidesRefresh(newItems);/// refresh listview
            }

            @Override
            public void onFail(String error) {

            }
        });

    }


    public static void getAllCountries(Context context) {
        UserOperations.getInstance(context).getAllCountries(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        long id = Long.parseLong(mJsonObject.getString("id"));
                        String name = mJsonObject.getString("name");
                        String alpha_2 = mJsonObject.getString("alpha_2");
                        String alpha_3 = mJsonObject.getString("alpha_3");

                        // Store in DB
                        CountryDAO.addNewCountry(id, name, alpha_2);
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
    public void getAllApps() {
        UserOperations.getInstance(context).getAllApps(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");////****
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

    public static void getAllMessages(Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        long id = Long.parseLong(mJsonObject.getString("id"));
                        long sender_id = Long.parseLong(mJsonObject.getString("sender_id"));
                        long receiver_id = Long.parseLong(mJsonObject.getString("receiver_id"));
                        String message = mJsonObject.getString("message");
                        long date_time = mJsonObject.getLong("date_time");

                        //store in DB
                        ChatDAO.addNewChat(id, message, sender_id, receiver_id, date_time);
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
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


    public static void userInfo(Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getUserInfo(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    long id = Long.parseLong(mJsonObject.getString("id"));
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

    public static  void userName(Context context, String id) {

        Map<String, String> params = new HashMap<>();
        params.put("username", id);
        UserOperations.getInstance(context).getUserName(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    long id = Long.parseLong(mJsonObject.getString("id"));
                    String username = mJsonObject.getString("username");

                    //store in DB


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

            }
        });
    }
    public static void find_a_ride(final OnSearchListener listener, final Context context, String item){
        Map<String, String> params = new HashMap<>();
        params.put("item", item);
        final ArrayList<Ride> allMatchedRides = new ArrayList<Ride>();
        UserOperations.getInstance(context).getSearchResult(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject){

                JSONArray mJsonArray = null;
                try {
                    mJsonArray = jsonObject.getJSONArray("json");
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
                        allMatchedRides.add(ride);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                }

                ///fetch user data for all matched rides
                    final Map<Ride, User> matchedRidesData = new HashMap<Ride, User>();
                    Map<String, String> params = new HashMap<>();
                    for(final Ride ride : allMatchedRides){
                        params.put("id", String.valueOf(ride.getUserId()));
                        UserOperations.getInstance(context).getPublicUserInfo(params, new OnLoadFinished() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                JSONArray mJsonArray = null;
                                try {
                                    mJsonArray = jsonObject.getJSONArray("json");
                                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                    User user = new User();
                                    user.username = mJsonObject.getString("username");
                                    user.phone = mJsonObject.getString("phone");
                                    user.image = mJsonObject.getString("img");
                                    matchedRidesData.put(ride, user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFail(String error) {

                            }
                        });
                        params.clear();
                    }
                    listener.onSearchSucceed(matchedRidesData);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public static void offerRide(Context context, final long user_id, final String city_from, final String city_to, final String state_from, final String state_to, final String country_from, final String country_to, final long date_time, final double price){
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("json");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    long remoteId = Long.parseLong(mJsonObject.getString("id"));

                    if (remoteId != -1) {
                       /* Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                        RideDAO.addNewRide(ride);*/
                        Log.i("Offer a Ride", "Succeeded");
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

    public static void last_chatting_users(final Context context, final OnChattingListListener listener, final long user_id, String username, String password) {

        final Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                JSONArray mJsonArray = jsonObject.getJSONArray("json");
                final ArrayList<User> all_users = new ArrayList<User>();
                for (int i = 0; i < mJsonArray.length(); i++) {
                    params.clear();
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        long id = Long.parseLong(mJsonObject.getString("id"));
                        long sender_id = Long.parseLong(mJsonObject.getString("sender_id"));
                        long receiver_id = Long.parseLong(mJsonObject.getString("receiver_id"));
                        String message = mJsonObject.getString("message");
                        long date_time = mJsonObject.getLong("date_time");
                        ChatDAO.addNewChat(id, message, sender_id, receiver_id, date_time);

                        if(sender_id != user_id)
                            params.put("id", String.valueOf(sender_id));
                        else
                            params.put("id", String.valueOf(receiver_id));

                    UserOperations.getInstance(context).getPublicUserInfo(params, new OnLoadFinished() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                JSONArray mJsonArray = jsonObject.getJSONArray("json");
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String name = mJsonObject.getString("username");
                                String email = mJsonObject.getString("email");
                                String phone = mJsonObject.getString("phone");
                                String image = mJsonObject.getString("img");
                                Long birthdate = mJsonObject.getLong("birthdate");
                                String gender = mJsonObject.getString("Gender");
                                UserDAO.addNewUser(Long.parseLong(params.get("id")), name, null, image, null, birthdate, gender, phone, email);
                                User user = UserDAO.getUserById(Long.parseLong(params.get("id")));

                                if(user != null) {
                                    boolean isAdded = false;
                                    for (User storedUser : all_users) {
                                        if (storedUser.getRemoteId() == Long.parseLong(params.get("id"))) {
                                            isAdded = true;
                                            break;
                                        }
                                    }
                                    if(!isAdded)
                                        all_users.add(user);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (InvalidInputException e) {
                                e.printStackTrace();
                            } catch (EmptyFieldException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });
                    listener.onChattingListRefreshed(all_users);

                }

                }catch (JSONException e) {
                    e.printStackTrace();
                } catch (EmptyFieldException e) {
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }
}
