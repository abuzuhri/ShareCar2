package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;

/**
 * Created by Bahaa on 15/9/2015.
 */
public interface OnSearchListener {
    public void onSearchSucceed(Map<Ride, User> newList);
    public void onSearchFailed(String error);
}
