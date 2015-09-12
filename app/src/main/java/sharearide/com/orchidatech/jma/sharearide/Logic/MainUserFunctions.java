package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 10/9/2015.
 */
public class MainUserFunctions {
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

    public static void signUp(Context context, String username, String password, String image, String address, long birthdate, String gender, String phone, String email ){

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

    public static void getAllRides(Context context){
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
                        ////store in DB
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
    public static void getAllCountries(Context context){
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
                        ///store in DB
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
    public static void getAllApps(Context context){
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
                        //store in DB
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
    public static void getAllMessages(Context context, String username, String password){

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
    public static void getAbout(Context context){
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
                    String face_account = mJsonObject.getString("facebook_account");
                    String twit_account = mJsonObject.getString("twitter_account");
                    String google_account = mJsonObject.getString("google_account");
                    ///store in DB


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }
    public static void userInfo(Context context, String username, String password){

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
                    //store in DB
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }
    public static String getUserName(Context context, long id){
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
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
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
        return null;
    }
}