package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;

/**
 * Created by Shadow on 8/30/2015.
 */
public class FindaRide extends AppCompatActivity {

    EditText fromCity, fromState, fromCountry;
    EditText toCity, toState, toCountry;
    EditText time, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.find_ride);

        /*
        fromCity = (EditText) findViewById(R.id.editText_fromCity);
        fromState = (EditText) findViewById(R.id.editText_fromState);
        fromCountry = (EditText) findViewById(R.id.editText_fromCountry);

        toCity = (EditText) findViewById(R.id.editText_toCity);
        toState = (EditText) findViewById(R.id.editText_toState);
        toCountry = (EditText) findViewById(R.id.editText_toCountry);

        time = (EditText) findViewById(R.id.editText_time_findRide);
        date = (EditText) findViewById(R.id.editText_date_findRide);
        */
    }


    public void searchClicked(View view) {

        Ride ride = new Ride();

        ride.fromCity = fromCity.getText().toString();
        ride.fromState = fromState.getText().toString();
        ride.fromCountry = fromCountry.getText().toString();

        ride.toCity = toCity.getText().toString();
        ride.toState = toState.getText().toString();
        ride.toCountry = toCountry.getText().toString();

        ride.dateTime = Long.valueOf(time.getText().toString());

        List<Ride> rides = RideDAO.searchForAllRides(ride);
        Intent intent = new Intent();
        // TODO: move rides data to search result
        //intent.putParcelableArrayListExtra("rides", rides);
    }
}
