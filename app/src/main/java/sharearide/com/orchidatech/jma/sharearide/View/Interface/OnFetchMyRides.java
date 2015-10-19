package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;

/**
 * Created by Bahaa on 7/10/2015.
 */
public interface OnFetchMyRides {
    public void onFetched(ArrayList<Ride> all_my_rides, int count, long last_id);
    public void onFailed(String error);
}
