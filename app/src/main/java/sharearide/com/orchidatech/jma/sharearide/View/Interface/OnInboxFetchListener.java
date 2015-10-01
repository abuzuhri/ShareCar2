package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import java.util.ArrayList;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;

/**
 * Created by Bahaa on 15/9/2015.
 */
public interface OnInboxFetchListener {
    public void onFetchInboxSucceed(ArrayList<Chat> allMessages, Map<Chat, ArrayList<User>> allMessagesData);
    public void onFetchInboxFailed(String error);

}
