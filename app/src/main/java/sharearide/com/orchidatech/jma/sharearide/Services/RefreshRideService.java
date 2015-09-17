package sharearide.com.orchidatech.jma.sharearide.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Bahaa on 13/9/2015.
 */
public class RefreshRideService extends IntentService {

    private static final int MAX_NUM_RIDES = 200;
    private  ArrayList<Ride> allFetchedRides;
    private ArrayList<Ride> allStoredRides;
    public RefreshRideService() {
        super(RefreshRideService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadNewData();
    }

    public void loadNewData(){
        final int MAX_NUM_RIDES = 200;

        ///load data from server DB...
        UserOperations.getInstance(getApplicationContext()).getAllRides(new OnLoadFinished() {
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
                        Ride ride = new Ride(remoteId, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                        RideDAO.addNewRide(ride);
                    }
                    ///      Log.i("RefreshRideService", allFetchedRides.size()+"");


                    ///To Ensure that max num of items in db is 200....
                    ArrayList<Ride> allStoredRides = new ArrayList<>(RideDAO.getAllRides());
                    int numOfRemovedRides = allStoredRides.size() - MAX_NUM_RIDES;
                    for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                        RideDAO.deleteRide(allStoredRides.get(delRideIndex).getId());

                } catch (JSONException e) {
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