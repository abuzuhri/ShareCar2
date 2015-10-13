package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;

/**
 * Created by Bahaa on 15/9/2015.
 */
public interface OnRidesListListener {
    public void onRidesRefresh(ArrayList<Ride> newItems);
    public void onRidesRefreshFailed(String error);
}
