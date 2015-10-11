package sharearide.com.orchidatech.jma.sharearide.Utility;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.RequestQueueHandler;

/**
 * Created by Bahaa on 14/9/2015.
 */
public class InternetConnectionChecker {

    public static void isConnectedToInternet(final Context context, final OnInternetConnectionListener listener){
    JsonObjectRequest request = new JsonObjectRequest(UrlConstant.CHECK_INTERNET_URL, null, new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject jsonObject) {
        try {
            jsonObject.getBoolean("accessed");
            listener.internetConnectionStatus(true);

        } catch (JSONException e) {
            listener.internetConnectionStatus(false);
            return;
        }
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError volleyError) {
    listener.internetConnectionStatus(false);
    return;
    }
});
        RequestQueueHandler.getInstance(context).addToRequestQueue(request);
//            new AsyncTask<Void, Void, Void>(){
//                    boolean status = false;
//                @Override
//                protected Void doInBackground(Void... voids) {
//
//                        Runtime runtime = Runtime.getRuntime();
//                    int responseCode = -1;
//
//                        try {
//                           /* InetAddress ipAddr = InetAddress.getByName("orchidatech.com"); //You can replace it with your name
//                           if(ipAddr.equals(""))
//                               status = false;
//                            else
//                                status = true;*/
//                            URL url = new URL("http://www.google.com");
//                            URLConnection connection = url.openConnection();
//                            connection.setConnectTimeout(20 * 1000);///5 seconds
//                            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
//                            responseCode = httpURLConnection.getResponseCode();
//                            if(responseCode == HttpURLConnection.HTTP_OK)
//                                status = true;
////
////                            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
////                            int value = ipProcess.waitFor();
////                            status =  (value == 0);
//                      //      Log.i("InternetConnectionChecker", status+"");
//
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                          e.printStackTrace();
//                            //Log.i("InternetConnectionChecker", e.getCause().toString());
//                          //  status = false;
//                        }
//
//                    return null;
//                    }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//                    listener.internetConnectionStatus(status);
//                }
//            }.execute();
    }

}
