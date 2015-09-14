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

    private static final int MAX_NUM_RIDES = 40;
    private  ArrayList<Ride> allFetchedRides;
    private ArrayList<Ride> allStoredRides;
    public RefreshRideService() {
        super(RefreshRideService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        allFetchedRides = new ArrayList<>();
        allStoredRides = new ArrayList<>(RideDAO.getAllRides());
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
                        allFetchedRides.add(ride);
                    }
              ///      Log.i("RefreshRideService", allFetchedRides.size()+"");

                     filterRides();
                     refreshRides();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {
                    //Log.i("RefreshService" , error);
            }
        });
              //  Log.i("RefreshRideService", "Service Running at " + System.currentTimeMillis());
    }

    private void refreshRides(){

        ///remove all un-needed rides
        if(allFetchedRides.size() < MAX_NUM_RIDES) {
            int numOfRemovedRides = (allFetchedRides.size() + allStoredRides.size()) - MAX_NUM_RIDES;
            Collections.reverse(allStoredRides);
            for (int delRideIndex = 0; delRideIndex < numOfRemovedRides; delRideIndex++)
                RideDAO.deleteRide(allStoredRides.get(delRideIndex).getId());
        }else {
            RideDAO.deleteAllRides();
        }

        ///add all new fetched rides to DB
        for(int newRideIndex = 0; newRideIndex < allFetchedRides.size(); newRideIndex++)
            try {
                RideDAO.addNewRide(allFetchedRides.get(newRideIndex));
            } catch (EmptyFieldException e) {
                e.printStackTrace();
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }

    }

    private void filterRides() {
        if(allFetchedRides.size() > MAX_NUM_RIDES)
            allFetchedRides = new ArrayList<>(allFetchedRides.subList(0, MAX_NUM_RIDES));
       else {
            Iterator<Ride> iterator = allFetchedRides.iterator();
            while (iterator.hasNext()) {
                Ride fetchedRide = iterator.next();
                for (Ride storedRide : allStoredRides) {
                    if (fetchedRide.getRemoteId() == storedRide.getRemoteId()) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }
}