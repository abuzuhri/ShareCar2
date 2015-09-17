package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;

/**
 * Created by Bahaa on 15/9/2015.
 */
public interface OnChattingListListener {
    public void onChattingListRefreshed(ArrayList<User> newList);

}
