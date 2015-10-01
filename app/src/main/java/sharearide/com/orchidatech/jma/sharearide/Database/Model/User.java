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

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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
