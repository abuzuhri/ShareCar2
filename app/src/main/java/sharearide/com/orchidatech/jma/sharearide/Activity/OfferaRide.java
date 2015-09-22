package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;

/**
 * Created by Shadow on 8/30/2015.
 */
public class OfferaRide extends AppCompatActivity {

    EditText fromCity, fromCountry;
    EditText toCity, toCountry;
    EditText time, date, cost;
    EditText name, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.find_ride);

        /*
        fromCity = (EditText) findViewById(R.id.editText_fromCity_offerRide);
        fromCountry = (EditText) findViewById(R.id.editText_fromCountry_offerRide);

        toCity = (EditText) findViewById(R.id.editText_toCity_offerRide);
        toCountry = (EditText) findViewById(R.id.editText_toCountry_offerRide);

        time = (EditText) findViewById(R.id.editText_time_offerRide);
        date = (EditText) findViewById(R.id.editText_date_offerRide);
        cost = (EditText) findViewById(R.id.editText_cost_offerRide);

        name = (EditText) findViewById(R.id.editText_name_offerRide);
        phone = (EditText) findViewById(R.id.editText_phone_offerRide);
        email = (EditText) findViewById(R.id.editText_email_offerRide);
        */
    }

    public void saveClicked(View view) {

        Ride ride = new Ride();

        /*
        ride.fromCity = fromCity.getText().toString();
        ride.fromCountry = fromCountry.getText().toString();
        ride.fromState = fromCountry.getText().toString();
        ride.toCountry = toCountry.getText().toString();

        ride.dateTime = Long.valueOf(dateTime.getText().toString());
        ride.cost = Double.parseDouble(cost.getText().toString());

        ride.user.name = name.getText().toString();
        ride.user.phone = Long.parseLong(phone.getText().toString());
        ride.user.email = email.getText().toString();
        */

        try {
            RideDAO.addNewRide(ride);
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
    }
}
