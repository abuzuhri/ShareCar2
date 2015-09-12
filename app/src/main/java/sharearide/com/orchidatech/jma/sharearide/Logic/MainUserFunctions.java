package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.ChatDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.CountryDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 10/9/2015.
 */
public class MainUserFunctions {
    private static String PREFS_NAME = "";

    private MainUserFunctions(){}

    public static void login(Context context,String username, String password){


        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).login(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    boolean success = mJsonObject.getBoolean("login");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }


    public static void signUp(Context context, String username, String password, String image, String address, long birthdate, String gender, String phone, String email) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("img", image);
        params.put("address", address);
        params.put("birthdate", String.valueOf(birthdate));
        params.put("Gender", gender);
        params.put("phone", phone);
        params.put("email", email);

        UserOperations.getInstance(context).signUp(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
                    JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                    boolean success = mJsonObject.getBoolean("signup");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }


    public  static void getAllRides(Context context) {
        UserOperations.getInstance(context).getAllRides(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        long id = Long.parseLong(mJsonObject.getString("id"));
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
                        Ride ride = null;

                        ride.remoteId = id;
                        ride.userId = user_id;
                        ride.fromCity = city_from;
                        ride.fromState = state_from;
                        ride.fromCountry = country_from;
                        ride.toCity = city_to;
                        ride.toState = state_to;
                        ride.toCountry = country_to;
                        ride.dateTime = date_time;
                        ride.cost = price;

                        RideDAO.addNewRide(ride);
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


    public static void getAllCountries(Context context) {
        UserOperations.getInstance(context).getAllCountries(new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
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
<<<<<<< HEAD
    public static void getAllMessages(Context context, String username, String password){
=======
    */

    public static void getAllMessages(Context context, String username, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        UserOperations.getInstance(context).getAllMessages(params, new OnLoadFinished() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
                    mJsonArray = jsonObject.getJSONArray("");
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
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
                    JSONArray mJsonArray = jsonObject.getJSONArray("");////****
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
}
