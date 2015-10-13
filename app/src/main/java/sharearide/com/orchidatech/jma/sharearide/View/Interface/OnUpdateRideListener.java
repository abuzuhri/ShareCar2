package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;

/**
 * Created by Bahaa on 7/10/2015.
 */
public interface OnUpdateRideListener {
    public void onFinished(Ride ride);
    public void onFailed(String error);

}
