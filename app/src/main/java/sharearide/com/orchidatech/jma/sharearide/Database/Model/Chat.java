package sharearide.com.orchidatech.jma.sharearide.Database.Model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Shadow on 9/6/2015.
 */

@Table(name = "Chats", id = BaseColumns._ID)
public class Chat extends Model {

    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "MsgId")
    public long msgId;

    @Column(name = "Message")
    public String message;

    @Column(name = "Sender")
    public User sender;

    @Column(name = "Receiver")
    public User receiver;

    @Column(name = "Date")
    public String date;

    @Column(name = "Address")
    public String address;


    public Chat() {
        super();
    }

    public List<User> users() {
        return getMany(User.class, "Ride");
    }
}
