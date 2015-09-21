package sharearide.com.orchidatech.jma.sharearide.Database.DAO;

import com.activeandroid.query.Select;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Country;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;

/**
 * Created by Shadow on 9/10/2015.
 */
public class CountryDAO {

    //<editor-fold defaultstate="collapsed" desc="addNewCountry(long countryId, String name, String alpha){...}">
    public static long addNewCountry(long countryId, String name, String alpha) throws EmptyFieldException, InvalidInputException {

        Country country = new Country();
        boolean isValid = true;

        if (countryId != 0) {
            country.remoteId = countryId;
        } else {
            isValid = false;
            throw new InvalidInputException("Invalid country id value !");
        }

        if (!name.equals("")) {
            country.name = name;
        } else {
            isValid = false;
            throw new EmptyFieldException("Country name is empty !");
        }

        if (!alpha.equals("")) {
            country.alpha = alpha;
        } else {
            isValid = false;
            throw new EmptyFieldException("Alpha name is empty !");
        }

        if (isValid) {
            return country.save();
        }

        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAllCountries(){...}">
    public static List<User> getAllCountries() {
        return new Select().from(Country.class).orderBy("Name").execute();
    }
    //</editor-fold>

}
