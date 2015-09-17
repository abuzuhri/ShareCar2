package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;

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
     ///   params.put("submit", Submit.LOGIN);
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.POST, UrlConstant.LOGIN_URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        //JSONObject jsonObject = new JSONObject(string);
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
                }, params);
    }

    public void signUp(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        ///params.put("submit", Submit.SIGNUP);
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.POST, UrlConstant.SIGNUP_URL, new Response.Listener<JSONObject>() {

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
                }, params);
    }

    public void getAllRides(final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.ALL_RIDES_URL, new Response.Listener<JSONObject>() {

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
                }, null);
    }

     public void getAllCountries(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.ALL_COUNTRIES_URL, new Response.Listener<JSONObject>() {
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
                }, null);
    }


    public void getAllApps(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.ALL_APPS_URL, new Response.Listener<JSONObject>() {

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
                }, null);
    }


    public void getAllMessages(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.POST, UrlConstant.ALL_MESSAGES_URL, new Response.Listener<JSONObject>() {

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
                }, params);
    }

    public void getAbout(final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.ABOUT_URL, new Response.Listener<JSONObject>() {

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
                }, null);
    }

    public void getUserInfo(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.POST, UrlConstant.USER_INFO, new Response.Listener<JSONObject>() {

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
                }, params);
    }

    public void getUserName(Map<String,String> params, final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.USER_NAME_URL, new Response.Listener<JSONObject>() {

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


                }, params);
    }

    public void getSearchResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.SEARCH_URL, new Response.Listener<JSONObject>() {

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
                }, params);
    }

    public void getPublicUserInfo(Map<String,String> params, final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.PUBLIC_USER_DATA, new Response.Listener<JSONObject>() {

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


                }, params);
    }


    public void offer_a_ride(Map<String, String> params, final OnLoadFinished onLoadFinished)    {
        UserOperationsProcessor.getInstance(context).sendRequest(Request.Method.GET, UrlConstant.ADD_RIDE_URL, new Response.Listener<JSONObject>() {

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
                }, params);
    }
/////////////////////////////////////////////////////////////////

    private static class Submit {
        public static final String LOGIN = "Log-In";
        public static final String SIGNUP = "SIGN-UP";
    }
}
