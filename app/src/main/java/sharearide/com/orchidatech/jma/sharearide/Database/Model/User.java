package sharearide.com.orchidatech.jma.sharearide.Database.Model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Shadow on 9/6/2015.
 */

@Table(name = "Users", id = BaseColumns._ID)
public class User extends Model {

    @Column(name = "remote_id", unique = true)
    public long remoteId;

    @Column(name = "Name")
    public String name;

    @Column(name = "Username")
    public String username;

    @Column(name = "Password")
    public String password;

    @Column(name = "Image")
    public String image;

    @Column(name = "Phone")
    public String phone;

    @Column(name = "Email")
    public String email;

    @Column(name = "Address")
    public String address;

    @Column(name = "Birthdate")
    public long birthdate;

    @Column(name = "Gender")
    public String gender;


    public User() {
        super();
    }

    // Used to return items from another table based on the foreign key
    public List<Ride> rides() {
        return getMany(Ride.class, "Ride");
    }

}
