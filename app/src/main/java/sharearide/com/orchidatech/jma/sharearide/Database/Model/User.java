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

    @Column(name = "remote_id") // , unique = true
    public long remoteId;

    public User(long remoteId, String name, String username, String password, String image, String phone, String email, String address, long birthdate, String gender) {
        this.remoteId = remoteId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

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
