package sharearide.com.orchidatech.jma.sharearide.Database.Model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

/**
 * Created by Shadow on 9/6/2015.
 */

@Table(name = "Rides", id = BaseColumns._ID)
public class Ride extends Model {

    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "RideId")
    public long rideId;

    @Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public User user;

    @Column(name = "FromCity")
    public String fromCity;

    @Column(name = "ToCity")
    public String toCity;

    @Column(name = "FromState")
    public String fromState;

    @Column(name = "ToState")
    public String toState;

    @Column(name = "FromCountry")
    public String fromCountry;

    @Column(name = "ToCountry")
    public String toCountry;

    @Column(name = "DateTime")
    public long dateTime;

    @Column(name = "Cost")
    public double cost;

    @Column(name = "Info")
    public String info;


    public Ride() {

    }

}
