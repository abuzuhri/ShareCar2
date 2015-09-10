package sharearide.com.orchidatech.jma.sharearide.Database.Model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Shadow on 9/6/2015.
 */

@Table(name = "Contacts", id = BaseColumns._ID)
public class Contact extends Model {

    @Column(name = "remote_id", unique = true)
    public long remoteId;

    @Column(name = "Name")
    public String name;

    @Column(name = "Phone")
    public long phone;

    @Column(name = "Email")
    public String email;

    public Contact() {

    }

}
