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
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ALL_MESSAGES_URL, new Response.Listener<JSONObject>() {

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

    public void getSearchResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.SEARCH_URL, new Response.Listener<JSONObject>() {

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
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.PUBLIC_USER_DATA, new Response.Listener<JSONObject>() {

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
        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ADD_RIDE_URL, new Response.Listener<JSONObject>() {

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
/////////////////////////////////////////////////////////////////

    private static class Submit {
        public static final String LOGIN = "Log-In";
        public static final String SIGNUP = "SIGN-UP";
    }
}
