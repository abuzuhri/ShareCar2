package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;

/**
 * Created by Shadow on 9/6/2015.
 */
public class RideDAO {

    public static long checkRideExist(long rideId) {
        Ride ride = new Select().from(Ride.class).where("UserId = ?", rideId).executeSingle();
        return ride.getId();
    }

    /**
     * @param r Ride instance
     */
    public static long addNewRide(Ride r) throws EmptyFieldException, InvalidInputException {

        Ride ride = new Ride();
        boolean isValid = true;

        //ride.rideId = r.rideId;
        //ride.user = r.user;

        if (!r.fromCity.equals("")) {
            ride.fromCity = r.fromCity;
        } else {
            isValid = false;
            throw new EmptyFieldException("From city field is empty !");
        }

        if (!r.fromState.equals("")) {
            ride.fromState = r.fromState;
        } else {
            isValid = false;
            throw new EmptyFieldException("From state field is empty !");
        }

        if (!r.fromCountry.equals("")) {
            ride.fromCountry = r.fromCountry;
        } else {
            isValid = false;
            throw new EmptyFieldException("From Country field is empty !");
        }

        if (!r.toCity.equals("")) {
            ride.toCity = r.toCity;
        } else {
            isValid = false;
            throw new EmptyFieldException("To City field is empty !");
        }

        if (!r.toState.equals("")) {
            ride.toState = r.toState;
        } else {
            isValid = false;
            throw new EmptyFieldException("To state field is empty !");
        }

        if (!r.toCountry.equals("")) {
            ride.toCountry = r.toCountry;
        } else {
            isValid = false;
            throw new EmptyFieldException("To country field is empty !");
        }

        if (r.dateTime == 0) {
            ride.dateTime = r.dateTime;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid date value !");
        }

        if (r.cost == 0) {
            ride.cost = r.cost;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid cost value !");
        }

        ride.info = r.info;

        if (!r.user.name.equals("")) {
            ride.user.name = r.user.name;
        } else {
            isValid = false;
            throw new InvalidInputException("Name field is empty !");
        }

        if (r.user.phone == 0) {
            ride.user.phone = r.user.phone;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid phone value !");
        }

        if (!r.user.email.equals("")) {
            ride.user.email = r.user.email;
        } else {
            isValid = false;
            throw new InvalidInputException("Email field is empty !");
        }

        if (isValid) {
            return ride.save();
        }
        return 0;
    }

    /**
     * @param id
     * @param r
     */
    public long updateRide(long id, Ride r) throws EmptyFieldException, InvalidInputException {

        Ride ride = Ride.load(Ride.class, id);
        boolean isValid = true;

        if (!r.fromCity.equals("")) {
            ride.fromCity = r.fromCity;
        } else {
            isValid = false;
            throw new EmptyFieldException("From city field is empty !");
        }

        if (!r.fromState.equals("")) {
            ride.fromState = r.fromState;
        } else {
            isValid = false;
            throw new EmptyFieldException("From state field is empty !");
        }

        if (!r.fromCountry.equals("")) {
            ride.fromCountry = r.fromCountry;
        } else {
            isValid = false;
            throw new EmptyFieldException("From Country field is empty !");
        }

        if (!r.toCity.equals("")) {
            ride.toCity = r.toCity;
        } else {
            isValid = false;
            throw new EmptyFieldException("To City field is empty !");
        }

        if (!r.toState.equals("")) {
            ride.toState = r.toState;
        } else {
            isValid = false;
            throw new EmptyFieldException("To state field is empty !");
        }

        if (!r.toCountry.equals("")) {
            ride.toCountry = r.toCountry;
        } else {
            isValid = false;
            throw new EmptyFieldException("To country field is empty !");
        }

        if (r.dateTime == 0) {
            ride.dateTime = r.dateTime;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid date value !");
        }

        if (r.cost == 0) {
            ride.cost = r.cost;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid cost value !");
        }

        ride.info = r.info;

        if (!r.user.name.equals("")) {
            ride.user.name = r.user.name;
        } else {
            isValid = false;
            throw new InvalidInputException("Name field is empty !");
        }

        if (r.user.phone == 0) {
            ride.user.phone = r.user.phone;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid phone value !");
        }

        if (!r.user.email.equals("")) {
            ride.user.email = r.user.email;
        } else {
            isValid = false;
            throw new InvalidInputException("Email field is empty !");
        }

        if (isValid) {
            return ride.save();
        }
        return 0;
    }


    /**
     * @param userId
     * @return
     */
    public static Ride getRideByUserId(long userId) {
        User user = new User();
        return new Select().from(Ride.class).where(user.userId + " = ?", userId).executeSingle();
    }

    /**
     * @param rideId
     * @return
     */
    public static Ride getRideById(long rideId) {
        return new Select().from(Ride.class).where("RideId = ?", rideId).executeSingle();
    }

    /**
     * @return
     */
    public static List<Ride> getAllRides() {
        return new Select().from(Ride.class).execute();
    }

    /**
     * @param rideId
     * @return
     */
    public static Ride searchForRide(long rideId) {
        return new Select().from(Ride.class).where("RideId = ?", rideId).orderBy("Cost").executeSingle();
    }

    /**
     * @param ride
     * @return
     */
    public static List<Ride> searchForAllRides(Ride ride) {
        return new Select()
                .from(Ride.class)
                .where("FromCity = ? AND ToCity = ?", ride.fromCity, ride.toCity)
                .execute();
    }

    /**
     * @param rideId
     */
    public void deleteRide(long rideId) {
        //  load an Item object by Id and delete it.
        Ride ride = Ride.load(Ride.class, rideId);
        ride.delete();

        // delete it statically
        //User.delete(User.class, id);

        // useing the query builder syntax
        //new Delete().from(User.class).where("Id = ?", id).execute();
    }

    /**
     *
     */
    public static void deleteAllRides() {
        new Delete().from(Ride.class).execute();
    }
}