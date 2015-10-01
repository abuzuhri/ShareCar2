package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;

/**
 * Created by Bahaa on 8/9/2015.
 */
public class UserOperations {

    private static UserOperations instance;
    private Context context;

    private UserOperations(Context context) {
        this.context = context;
    }

    public static synchronized UserOperations getInstance(Context context) {
        if (instance == null)
            instance = new UserOperations(context);
        return instance;
    }
    public void login(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.LOGIN_URL + "?username=" + params.get("username")+"&password="+params.get("password");
        Log.i("url", url);
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void signUp(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.SIGNUP_URL + "?username=" + params.get("username")+"&password="+params.get("password")
               +"&img="+params.get("img") +"&address="+params.get("address")+"&brithdate="+params.get("birthdate")
                +"&phone="+params.get("phone")+"&Gender="+params.get("Gender")+"&email="+params.get("email");
        Log.i("Signup", url);
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getAllRides(final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ALL_RIDES_URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

     public void getAllCountries(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ALL_COUNTRIES_URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }


    public void getAllApps(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ALL_APPS_URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }


    public void getAllMessages(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.ALL_MESSAGES_URL + "?username=" + params.get("username")+"&password="+params.get("password");
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getAbout(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ABOUT_URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getUserInfo(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.USER_INFO, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getUserName(Map<String,String> params, final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.USER_NAME_URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }


                });
    }


    public void getSearchAllResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.SEARCH_ALL_URL +"?item=" + params.get("item");

        Log.i("Ride", url);
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getSearchResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.SEARCH_URL +"?city_from=" + params.get("city_from")
                +"&city_to=" + params.get("city_to")
                +"&state_from=" + params.get("state_from")
                +"&state_to=" + params.get("state_to")
                +"&country_from=" + params.get("country_from")
                +"&country_to=" + params.get("country_to")
                +"&date_time=" + params.get("date_time");
        Log.i("Ride", url);
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }

    public void getPublicUserInfo(Map<String,String> params, final OnLoadFinished onLoadFinished){
        String url = UrlConstant.PUBLIC_USER_DATA + "?id="+params.get("id");
        Log.i("getPublicUserInfo", url);
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {

                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }


                });
    }


    public void offer_a_ride(Map<String, String> params, final OnLoadFinished onLoadFinished)    {
        String url = UrlConstant.ADD_RIDE_URL +"?user_id=" + params.get("user_id")
                                            +"&city_from=" + params.get("city_from")
                                            +"&city_to=" + params.get("city_to")
                                            +"&state_from=" + params.get("state_from")
                                            +"&state_to=" + params.get("state_to")
                                            +"&country_from=" + params.get("country_from")
                                            +"&country_to=" + params.get("country_to")
                                            +"&price=" + params.get("price")
                                            +"&date_time=" + params.get("date_time");
        Log.i("Ride", url);

        /* params.put("user_id", String.valueOf(user_id));
        params.put("city_from", city_from);
        params.put("city_to", city_to);
        params.put("state_from", state_from);
        params.put("state_to", state_to);
        params.put("country_from", country_from);
        params.put("country_to", country_to);
        params.put("date_time", String.valueOf(date_time));
        params.put("price", String.valueOf(price));*/
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }


    public void addMessage(Map<String, String> params, final OnLoadFinished onLoadFinished){
        String url = UrlConstant.ADD_MESSAGE_URL + "?message=" + params.get("message")+"&sender_id="+params.get("sender_id")+"&receiver_id="+params.get("receiver_id");
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }


    public void forgetPassword(Map<String, String> params, final OnLoadFinished onLoadFinished){
        String url = UrlConstant.FORGET_PASSWORD_URL + "?email=" + params.get("email");
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(volleyError.getMessage());
                    }
                });
    }
    private static void isConnected(final Context context){

    }
/////////////////////////////////////////////////////////////////

}
