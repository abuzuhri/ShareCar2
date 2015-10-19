package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
        String url = UrlConstant.LOGIN_URL + "?email=" + params.get("email")+"&password="+params.get("password");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public void signUp(String url, Map<String, String> params, final OnLoadFinished onLoadFinished) {
        url += "?username=" + params.get("username")+"&password="+params.get("password")
                +"&img="+params.get("img") +"&address="+params.get("address")+"&brithdate="+params.get("birthdate")
                +"&phone="+params.get("phone")+"&Gender="+params.get("Gender")+"&email="+params.get("email");
        if(params.get("social_id") != null)
            url +="&social_id="+params.get("social_id");
        // String s=params.get("img");
        url=  url.replace(" ","%20");//space between fname & lname
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public void getAllRides(final OnLoadFinished onLoadFinished){
        sendRequest(UrlConstant.ALL_RIDES_URL, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ALL_RIDES_URL, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }
    public void get_my_rides(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.GET_MY_RIDES_URL +"?user_id=" + params.get("user_id");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public  void uploadImage(final Map<String, String> params, final OnLoadFinished onLoadFinished) {
        ///  String url = UrlConstant.UPLOAD_IMAGE_URL + "?user_id=" + params.get("user_id") + "&image=" + params.get("image");
        /// Log.i("Ride", url);
        // url=url.replace(" ","%20");
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, UrlConstant.UPLOAD_IMAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject json = new JSONObject(s);
                    onLoadFinished.onSuccess(json);
                } catch (JSONException e) {
                    onLoadFinished.onFail("An error occurred, try again");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onLoadFinished.onFail("Can not connect server");

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueueHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void updateRide(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.UPDATE_RIDE_URL +"?ride_id=" + params.get("ride_id")
                +"&city_from=" + params.get("city_from")
                +"&city_to=" + params.get("city_to")
                +"&state_from=" + params.get("state_from")
                +"&state_to=" + params.get("state_to")
                +"&country_from=" + params.get("country_from")
                +"&country_to=" + params.get("country_to")
                +"&price=" + params.get("price")
                +"&date_time=" + params.get("date_time")
                +"&from_latitude="+params.get("from_latitude")
                +"&from_longitude=" +params.get("from_longitude")
                +"&to_latitude="+params.get("to_latitude")
                +"&to_longitude="+params.get("to_longitude")
                +"&more_info="+params.get("more_info");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }
    public void deleteRide(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.DELETE_RIDE_URL +"?ride_id=" + params.get("ride_id");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }



    public void getAllMessages(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.ALL_MESSAGES_URL + "?user_id=" + params.get("user_id");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public void getAbout(final OnLoadFinished onLoadFinished) {
        sendRequest(UrlConstant.ABOUT_URL, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(UrlConstant.ABOUT_URL, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }


    public void getSearchAllResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.SEARCH_ALL_URL +"?item=" + params.get("item")
                +"&user_id=" + params.get("user_id")
                +"&last_id=" + params.get("last_id")
                +"&offset=" + params.get("offset");
        url=url.replace(" ","%20");
         Log.i("Ride", url);
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public void getSearchResult(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.SEARCH_URL +"?city_from=" + params.get("city_from")
                +"&city_to=" + params.get("city_to")
                +"&state_from=" + params.get("state_from")
                +"&state_to=" + params.get("state_to")
                +"&country_from=" + params.get("country_from")
                +"&country_to=" + params.get("country_to")
                +"&user_id=" + params.get("user_id")
                +"&last_id=" + params.get("last_id")
                +"&offset=" + params.get("offset");
        url=url.replace(" ","%20");
        Log.i("urrl", url);
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
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
                +"&date_time=" + params.get("date_time")
                +"&from_latitude="+params.get("from_latitude")
                +"&from_longitude=" +params.get("from_longitude")
                +"&to_latitude="+params.get("to_latitude")
                +"&to_longitude="+params.get("to_longitude")
                +"&more_info="+params.get("more_info");
        url=url.replace(" ","%20");

        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }


    public void addMessage(Map<String, String> params, final OnLoadFinished onLoadFinished){
        String url = UrlConstant.ADD_MESSAGE_URL + "?message=" + params.get("message")+"&sender_id="+params.get("sender_id")+"&receiver_id="+params.get("receiver_id") + "&date_time=" + params.get("date_time");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }


    public void forgetPassword(Map<String, String> params, final OnLoadFinished onLoadFinished){
        String url = UrlConstant.FORGET_PASSWORD_URL + "?email=" + params.get("email");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);

//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }


    public void getAddress(Map<String, String> params, final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.GET_ADDRESS_URL + "?format=" + params.get("format") + "&lat=" + params.get("lat") + "&lon=" + params.get("lon");
        url=url.replace(" ","%20");
        sendRequest(url, onLoadFinished);


//        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject o) {
//                        try {
//                            onLoadFinished.onSuccess(o);
//                        } catch (JSONException e) {
//                            onLoadFinished.onFail("An error occurred, try again");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        onLoadFinished.onFail("Can not connect server");
//                    }
//                });
    }

    public void updateProfile(Map<String, String> params,final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.UPDATE_PROFILE_URL + "?user_id=" + params.get("user_id") + "&email=" + params.get("email") + "&phone=" + params.get("phone") + "&password=" + params.get("password") ;
        url=url.replace(" ","%20");
       // Log.i("updateProfile", url);
    sendRequest(url, onLoadFinished);

    }
    public void getLastRecord(Map<String, String> params,final OnLoadFinished onLoadFinished) {
        String url = UrlConstant.GET_LAST_RIDE + "?user_id=" + params.get("user_id");
        url=url.replace(" ","%20");
        // Log.i("updateProfile", url);
        sendRequest(url, onLoadFinished);

    }

    private void sendRequest(final String url, final OnLoadFinished onLoadFinished){
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            onLoadFinished.onFail("An error occurred, try again");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail("Can not connect server");
                    }
                });
    }

}
