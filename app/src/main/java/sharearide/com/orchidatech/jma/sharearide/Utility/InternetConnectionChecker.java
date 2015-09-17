package sharearide.com.orchidatech.jma.sharearide.Utility;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import sharearide.com.orchidatech.jma.sharearide.Constant.Constant;
import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;

/**
 * Created by Bahaa on 14/9/2015.
 */
public class InternetConnectionChecker {

    public static void isConnectedToInternet(final OnInternetConnectionListener listener){
            new AsyncTask<Void, Void, Void>(){
                    boolean status = true;
                @Override
                protected Void doInBackground(Void... voids) {

                        Runtime runtime = Runtime.getRuntime();
                        try {
                           /* InetAddress ipAddr = InetAddress.getByName("orchidatech.com"); //You can replace it with your name
                           if(ipAddr.equals(""))
                               status = false;
                            else
                                status = true;*/

                            //new URL(UrlConstant.SERVER_URL).openStream();

                            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                            int value = ipProcess.waitFor();
                            status =  (value == 0);
                            Log.i("InternetConnectionChecker", status+"");

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                          e.printStackTrace();
                            //Log.i("InternetConnectionChecker", e.getCause().toString());
                          //  status = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    return null;
                    }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    listener.internetConnectionStatus(status);
                }
            }.execute();
    }

}
