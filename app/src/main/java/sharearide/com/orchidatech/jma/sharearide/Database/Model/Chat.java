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

    @Column(name = "Name")
    public String name;

    @Column(name = "Message")
    public String message;

    @Column(name = "SenderId")
    public long senderId;

    @Column(name = "ReceiverId")
    public long receiverId;

    @Column(name = "DateTime")
    public long dateTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    @Column(name = "Address")
    public String address;


    public Chat() {
        super();
    }

    public List<User> users() {
        return getMany(User.class, "Ride");
    }
}
