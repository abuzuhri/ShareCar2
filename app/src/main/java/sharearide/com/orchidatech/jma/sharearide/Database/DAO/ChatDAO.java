package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;

/**
 * Created by Shadow on 9/6/2015.
 */

public class ChatDAO {

    public static long checkChatExist(long userId) {
        Chat chat = new Select().from(Chat.class).where("remote_id = ?", userId).executeSingle();
        return chat.getId();
    }

    public static long addNewChat(long msgId, String message, long senderId, long receiverId, long dateTime)
            throws EmptyFieldException, InvalidInputException {

        Chat chat = new Chat();
        boolean isValid = true;

        if (!message.equals("")) {
            chat.message = message;
        } else {
            isValid = false;
            throw new EmptyFieldException("Empty message field !");
        }

        if (senderId == 0) {
            chat.senderId = senderId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid sender id !");
        }

        if (receiverId == 0) {
            chat.receiverId = receiverId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid receiver id !");
        }

        if (dateTime == 0) {
            chat.dateTime = dateTime;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid dateTime value");
        }

        if (isValid) {
            return chat.save();
        }

        return 0;
    }

    public static long updateChat(long msgId, String message, long senderId, long receiverId, long dateTime)
            throws EmptyFieldException, InvalidInputException {

        Chat chat = Chat.load(Chat.class, msgId);
        boolean isValid = true;

        if (!message.equals("")) {
            chat.message = message;
        } else {
            isValid = false;
            throw new EmptyFieldException("Empty message field !");
        }

        if (senderId == 0) {
            chat.senderId = senderId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid sender id !");
        }

        if (receiverId == 0) {
            chat.receiverId = receiverId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid receiver id !");
        }

        if (dateTime == 0) {
            chat.dateTime = dateTime;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid dateTime value");
        }

        if (isValid) {
            return chat.save();
        }

        return 0;
    }

    public static void deleteChat(long msgId) {
        //  load an Item object by Id and delete it.
        Chat chat = Chat.load(Chat.class, msgId);
        chat.delete();

        // delete it statically
        //Chat.delete(Chat.class, id);

        // useing the query builder syntax
        //new Delete().from(Chat.class).where("Id = ?", id).execute();
    }

    public static Chat searchForChat(long chatId) {
        return new Select().from(Chat.class).where("remote_id = ?", chatId).executeSingle();
    }

    public static List<User> getAllChats() {
        return new Select().from(User.class).orderBy("DateTime").execute();
    }

    public static List<Ride> getAllChats(Chat chat) {
        return new Select()
                .from(Chat.class)
                .where("Sender = ? AND Receiver = ?", chat.senderId, chat.receiverId)
                .execute();
    }

}