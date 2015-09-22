package sharearide.com.orchidatech.jma.sharearide.Activity;

/**
 * Created by Amal on 9/17/2015.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;

public class AddRide extends Fragment {
private Button save,more_info;
    private Context context;
    private EditText cityFrom,cityTo,countryFrom,countryTo,stateFrom,stateTo,time,date;
    private EditText price;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.add_ride,container,false);
        save=(Button)v.findViewById(R.id.save);
        more_info=(Button)v.findViewById(R.id.more_info);
        cityFrom=(EditText)v.findViewById(R.id.cityFrom);
        cityTo=(EditText)v.findViewById(R.id.cityTo);
        countryFrom=(EditText)v.findViewById(R.id.countryFrom);
        countryTo=(EditText)v.findViewById(R.id.countryTo);
        stateFrom=(EditText)v.findViewById(R.id.stateFrom);
        stateTo=(EditText)v.findViewById(R.id.stateTo);
        time=(EditText)v.findViewById(R.id.time);
        date=(EditText)v.findViewById(R.id.date);
        price=(EditText)v.findViewById(R.id.price);
context = getActivity();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityFrom.getText().toString().equals("")) {
                    cityFrom.setError("Required Field");
                } else if (countryFrom.getText().toString().equals(""))
                    countryFrom.setError("Required Field");
//                else if (time.getText().toString().equals(""))
//                    time.setError("Required Field ");
//                else if (date.getText().toString().equals(""))
//                    date.setError("Required Field ");
                else if (cityTo.getText().toString().equals(""))
                    cityTo.setError("Required Field ");
                else if (countryTo.getText().toString().equals(""))
                    countryTo.setError("Required Field ");
                else{
                    offerRide(context.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1),
                            cityFrom.getText().toString(), cityTo.getText().toString(),
                            stateFrom.getText().toString(), stateTo.getText().toString(),
                            countryFrom.getText().toString(), countryTo.getText().toString(),
                            -1, Double.parseDouble(price.getText().toString())
                            );

            }}
        });

        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(getActivity(),MoreInfo.class);
              startActivity(i);
            }
        });
        return v;
    }

    public void offerRide( final long user_id,  final String city_from,  final String city_to,  final String state_from,  final String state_to,  final String country_from,  final String country_to,  final long date_time,  final double price)
    {
        InternetConnectionChecker.isConnectedToInternet(context, new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status)
                    MainUserFunctions.offerRide(context, user_id, city_from, city_to, state_from, state_to, country_from, country_to, date_time, price);
                else
                    Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
            }
        });
    }
}