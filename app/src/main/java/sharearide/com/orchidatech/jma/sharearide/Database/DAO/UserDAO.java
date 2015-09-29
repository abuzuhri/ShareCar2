package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmailValidator;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;

/**
 * Created by Shadow on 9/6/2015.
 */
public class UserDAO {

    public static final String TABLE_NAME = "Users";

    public static final String NAME = "name";

    //<editor-fold defaultstate="collapsed" desc="addNewUser(long userId, String username, String password, String image, String address, long birthdate, String gender, String phone, String email){...}">
   public static long addNewUser(User user) throws EmptyFieldException, InvalidInputException {
            if(checkUserExist(user.getRemoteId()) == -1)
                return user.save();
        else
                return user.getRemoteId();
   }
    public static long addNewUser(long userId, String username, String password, String image,
                                  String address, long birthdate, String gender, String phone, String email)
            throws EmptyFieldException, InvalidInputException {

        User user = new User();
        boolean isValid = true;

        EmailValidator emailValidator = new EmailValidator();

        if (userId != 0) {
            user.remoteId = userId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid user id !");
        }

        if (!username.equals("")) {
            user.username = username;
        } else {
            isValid = false;
            throw new EmptyFieldException("Username field is empty !");
        }

        if (!password.equals("")) {
            user.password = password;
        } else {
            isValid = false;
            throw new EmptyFieldException("Password field is empty !");
        }

        // TODO: check for validation
        user.image = image;
        user.address = address;
        user.birthdate = birthdate;
        user.gender = gender;
        user.phone = phone;

        if (emailValidator.isValidEmailAddress(email)) {
            user.email = email;
        } else {
            isValid = false;
            throw new EmptyFieldException("Email is invalid !");
        }

        if (isValid) {
            return user.save();
        }
        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkUserExist(long userId){...}">

    public static long checkUserExist(long userId) {
        User user = new Select().from(User.class).where("remote_id = ?", userId).executeSingle();
        if(user != null)
             return user.getId();
        else
            return -1;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="signIn(String un, String pass){...}">
    public boolean signIn(String username, String password) {

        User user = null;

        if (!username.equals("") && !password.equals("")) {
            user = new Select()
                    .from(User.class)
                    .where("Username = ? AND Password = ?", username, password)
                    .executeSingle();

            if (user != null)
                return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="retreivePassword(String email){...}">
    public static String retreivePassword(String email) {
        User user = new Select("Password").from(User.class).where("Email = ?", email).executeSingle();
        return user.password;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="signUp(String username, String password, String image, String address, long birthdate, String gender, String phone, String email){...}">
    public static long signUp(String username, String password, String image, String address,
                              long birthdate, String gender, String phone, String email)
            throws EmptyFieldException, InvalidInputException {

        User user = new User();
        boolean isValid = true;

        EmailValidator emailValidator = new EmailValidator();

        /*
        if (userId != 0) {
            user.remoteId = userId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid user id !");
        }
        */

        if (!username.equals("")) {
            user.username = username;
        } else {
            isValid = false;
            throw new EmptyFieldException("Username field is empty !");
        }

        if (!password.equals("")) {
            user.password = password;
        } else {
            isValid = false;
            throw new EmptyFieldException("Password field is empty !");
        }

        // TODO: check for validation
        user.image = image;
        user.address = address;
        user.birthdate = birthdate;
        user.gender = gender;
        user.phone = phone;

        if (emailValidator.isValidEmailAddress(email)) {
            user.email = email;
        } else {
            isValid = false;
            throw new EmptyFieldException("Email is invalid !");
        }

        if (isValid) {
            return user.save();
        }
        return 0;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SignUp(String name, String password, String repassword, String email){...}">
    public static long SignUp(String name, String password, String repassword, String email) throws EmptyFieldException {

        User user = new User();
        boolean isValid = true;

        EmailValidator emailValidator = new EmailValidator();

        if (!name.equals("")) {
            user.name = name;
        } else {
            isValid = false;
            throw new EmptyFieldException("Name field is empty !");
        }

        if (!password.equals("") && !repassword.equals("") && repassword.equals(password)) {
            user.password = password;
        } else {
            isValid = false;
            throw new EmptyFieldException("Password field is empty !");
        }

        if (emailValidator.isValidEmailAddress(email)) {
            user.email = email;
        } else {
            isValid = false;
            throw new EmptyFieldException("Email is invalid !");
        }

        if (isValid) {
            return user.save();
        }
        return 0;
    }


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateUser(long userId, String name, String password, String email){...}">
    public static long updateUser(long userId, String username, String password, String image,
                                  String address, long birthdate, String gender, String phone, String email)
            throws EmptyFieldException, InvalidInputException {

        User user = User.load(User.class, userId);

        boolean isValid = true;

        EmailValidator emailValidator = new EmailValidator();

        if (userId != 0) {
            user.remoteId = userId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid user id !");
        }

        if (!username.equals("")) {
            user.username = username;
        } else {
            isValid = false;
            throw new EmptyFieldException("Username field is empty !");
        }

        if (!password.equals("")) {
            user.password = password;
        } else {
            isValid = false;
            throw new EmptyFieldException("Password field is empty !");
        }

        // TODO: check for validation
        user.image = image;
        user.address = address;
        user.birthdate = birthdate;
        user.gender = gender;
        user.phone = phone;

        if (emailValidator.isValidEmailAddress(email)) {
            user.email = email;
        } else {
            isValid = false;
            throw new EmptyFieldException("Email is invalid !");
        }

        if (isValid) {
            return user.save();
        }
        return 0;
    }

    //</editor-fold>


    public static void addNewSocialUser(SocialUser socialUser){
        User user = new User();
        user.email = socialUser.getEmail();
        user.image = socialUser.getAvatarURL();
        user.username = socialUser.getName();
        user.save();
    }

    //<editor-fold defaultstate="collapsed" desc="getUserById(long userId){...}">
    public static User getUserById(long userId) {
        return new Select().from(User.class).where("remote_id = ?", userId).executeSingle();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAllUsers(){...}">

    /**
     * @return
     */
    public static List<User> getAllUsers() {
        return new Select().from(User.class).orderBy(NAME).execute();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getUserRides(long userId){...}">
    public static List<Ride> getUserRides(long userId) {
        User user = User.load(User.class, userId);
        //User u = getUserById(id);
        return new Select().from(Ride.class).where("User = ?", user).orderBy("User ASC").execute();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="searchForUserById(long userId){...}">
    public static Ride searchForUserById(long userId) {
        return new Select().from(User.class).where("UserId = ?", userId).orderBy("Name").executeSingle();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="searchForUserByName(String name){...}">
    public static Ride searchForUserByName(String name) {
        return new Select().from(User.class).where("Name = ?", name).orderBy("Name").executeSingle();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="searchForUserByEmail(String email){...}">
    public static Ride searchForUserByEmail(String email) {
        return new Select().from(User.class).where("Email = ?", email).orderBy("Name").executeSingle();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteUser(long userId){...}">
    public static void deleteUser(long userId) {
        //  load an Item object by Id and delete it.
        User user = User.load(User.class, userId);
        user.delete();

        // delete it statically
        //User.delete(User.class, id);

        // useing the query builder syntax
        //new Delete().from(User.class).where("Id = ?", id).execute();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteAllUsers(){...}">
    public static void deleteAllUsers() {
        new Delete().from(User.class).execute();
    }
    //</editor-fold>

}
