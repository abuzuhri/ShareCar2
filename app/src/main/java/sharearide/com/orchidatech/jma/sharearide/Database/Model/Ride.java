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

    @Column(name = "UserId", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public long userId;

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

    public Ride(long remoteId, long userId, String fromCity, String toCity, String fromState, String toState, String fromCountry, String toCountry, long dateTime, double cost) {
        this.remoteId = remoteId;
        this.userId = userId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.fromState = fromState;
        this.toState = toState;
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.dateTime = dateTime;
        this.cost = cost;
    }

    @Column(name = "Cost")
    public double cost;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    @Column(name = "Info")

    public String info;


    public Ride() {

    }

}
