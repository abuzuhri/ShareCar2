package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;

/**
 * Created by Shadow on 9/6/2015.
 */

public class ChatDAO {

    public void addNewMsg(long msgId, String message, User sender, User receiver, String date) {

        Chat chat = new Chat();
        chat.msgId = msgId;
        chat.message = message;
        chat.sender = sender;
        chat.receiver = receiver;

        // TODO: date format
        chat.date = date;

        chat.save();
    }

    public void updateMsg(long msgId, String message, User sender, User receiver) {

        Chat chat = Chat.load(Chat.class, msgId);
        chat.msgId = msgId;
        chat.message = message;
        chat.sender = sender;
        chat.receiver = receiver;

        chat.save();
    }

    public void deleteChat(long msgId) {
        //  load an Item object by Id and delete it.
        Chat chat = Chat.load(Chat.class, msgId);
        chat.delete();

        // delete it statically
        //Chat.delete(Chat.class, id);

        // useing the query builder syntax
        //new Delete().from(Chat.class).where("Id = ?", id).execute();
    }

    public static Chat searchForChat(long chatId) {
        return new Select().from(Chat.class).where("ChatId = ?", chatId).executeSingle();
    }

    public static List<User> getAllChats() {
        return new Select().from(User.class).orderBy("Date").execute();
    }

}