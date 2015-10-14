
package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;

/**
 * Created by Shadow on 9/6/2015.
 */
public class RideDAO {

    public static long checkRideExist(long rideId) {
        Ride ride = new Select().from(Ride.class).where("remote_id = ?", rideId).executeSingle();
        if(ride != null)
            return ride.getId();
        return -1;
    }

    public static long addNewRide(long remote_id, long userId, String fromCity, String toCity, String fromCountry,
                                  String toCountry, String fromState, String toState, long dateTime, String cost,
                                  String more_info, double from_Longitude, double to_longitude, double from_Lattitude, double to_latitude) {
//    if(checkRideExist(r.getRemoteId()) != -1)
//        return r.getRemoteId();

        Ride ride = getRideByRemoteId(remote_id);
        if(ride == null){
            ride = new Ride();
            ride.remoteId = remote_id;
        }

        ride.userId = userId;
        ride.fromCity = fromCity;
        ride.fromState = fromState;
        ride.fromCountry = fromCountry;
        ride.toCity = toCity;
        ride.toState = toState;
        ride.toCountry = toCountry;
        ride.dateTime = dateTime;
        ride.cost = cost;
        ride.to_longitude= to_longitude;
        ride.to_latitude= to_latitude;
        ride.from_Lattitude=from_Lattitude;
        ride.from_Longitude=from_Longitude;
        ride.more_info = more_info;
        return ride.save();
    }

//
//    public long updateRide(long id, Ride r) throws EmptyFieldException, InvalidInputException {
//
//        Ride ride = Ride.load(Ride.class, id);
//        boolean isValid = true;
//
//        if (!r.fromCity.equals("")) {
//            ride.fromCity = r.fromCity;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("From city field is empty !");
//        }
//
//        if (!r.fromState.equals("")) {
//            ride.fromState = r.fromState;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("From state field is empty !");
//        }
//
//        if (!r.fromCountry.equals("")) {
//            ride.fromCountry = r.fromCountry;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("From Country field is empty !");
//        }
//
//        if (!r.toCity.equals("")) {
//            ride.toCity = r.toCity;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("To City field is empty !");
//        }
//
//        if (!r.toState.equals("")) {
//            ride.toState = r.toState;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("To state field is empty !");
//        }
//
//        if (!r.toCountry.equals("")) {
//            ride.toCountry = r.toCountry;
//        } else {
//            isValid = false;
//            throw new EmptyFieldException("To country field is empty !");
//        }
//
//        if (r.dateTime == 0) {
//            ride.dateTime = r.dateTime;
//        } else {
//            isValid = false;
//            throw new InvalidInputException("Invalid date value !");
//        }
//
//        if (r.cost == 0) {
//            ride.cost = r.cost;
//        } else {
//            isValid = false;
//            throw new InvalidInputException("Invalid cost value !");
//        }
//
//        ride.info = r.info;
//
//        /*
//        if (!r.user.name.equals("")) {
//            ride.user.name = r.user.name;
//        } else {
//            isValid = false;
//            throw new InvalidInputException("Name field is empty !");
//        }
//        if (r.user.phone == 0) {
//            ride.user.phone = r.user.phone;
//        } else {
//            isValid = false;
//            throw new InvalidInputException("Invalid phone value !");
//        }
//        if (!r.user.email.equals("")) {
//            ride.user.email = r.user.email;
//        } else {
//            isValid = false;
//            throw new InvalidInputException("Email field is empty !");
//        }
//        */
//
//        if (isValid) {
//            return ride.save();
//        }
//        return 0;
//    }

    public static Ride getRideByUserId(long userId) {
        User user = new User();
        return new Select().from(Ride.class).where(user.getId() + " = ?", userId).executeSingle();
    }

    public static Ride getRideById(long rideId) {
//        return new Select().from(Ride.class).where("remote_id = ?", rideId).executeSingle();
        return Ride.load(Ride.class, rideId);
    }

    public static Ride getRideByRemoteId(long rideRemoteId) {
        return new Select().from(Ride.class).where("remote_id = ?", rideRemoteId).executeSingle();
    }

    public static List<Ride> getAllRides() {
        return new Select().from(Ride.class).orderBy("DateTime ASC").execute();
    }

    public static Ride searchForRide(long rideId) {
        return new Select().from(Ride.class).where("RideId = ?", rideId).orderBy("Cost").executeSingle();
    }

    public static List<Ride> searchForAllRides(Ride ride) {
        return new Select()
                .from(Ride.class)
                .where("FromCity = ? AND ToCity = ?", ride.fromCity, ride.toCity)
                .execute();
    }

    public static void deleteRide(long rideId) {
    Ride ride = getRideByRemoteId(rideId);
        if(ride != null)
            ride.delete();

        // delete it statically
        //User.delete(User.class, id);

        // useing the query builder syntax
        //new Delete().from(User.class).where("Id = ?", id).execute();
    }

    public static void deleteAllRides() {
        new Delete().from(Ride.class).execute();
    }

    public static void updateRide(Ride _ride) {
        Ride ride = _ride;
        ride.save();
    }
}