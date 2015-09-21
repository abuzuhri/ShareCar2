package sharearide.com.orchidatech.jma.sharearide.Utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import sharearide.com.orchidatech.jma.sharearide.Constant.Constant;
import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.RequestQueueHandler;

/**
 * Created by Bahaa on 14/9/2015.
 */
public class InternetConnectionChecker {

    public static void isConnectedToInternet(final Context context, final OnInternetConnectionListener listener){
StringRequest request = new StringRequest("http://www.google.com", new Response.Listener<String>() {
    @Override
    public void onResponse(String s) {
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        listener.internetConnectionStatus(true);
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError volleyError) {
//        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
    listener.internetConnectionStatus(false);
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
