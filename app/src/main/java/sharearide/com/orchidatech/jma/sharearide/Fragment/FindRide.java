package sharearide.com.orchidatech.jma.sharearide.Fragment;

/**
 * Created by Amal on 9/17/2015.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sharearide.com.orchidatech.jma.sharearide.Activity.SearchResult;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;

/**
 * Created by Edwin on 15/02/2015.
 */
public class FindRide extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText cityFrom, cityTo, countryFrom, countryTo, stateFrom, stateTo;
    private Button search;
    private Calendar calendar;
    Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_ride, container, false);

        cityFrom = (EditText) v.findViewById(R.id.cityFrom);
        cityTo = (EditText) v.findViewById(R.id.cityTo);
        countryFrom = (EditText) v.findViewById(R.id.countryFrom);
        countryTo = (EditText) v.findViewById(R.id.countryTo);
        stateFrom = (EditText) v.findViewById(R.id.stateFrom);
        stateTo = (EditText) v.findViewById(R.id.stateTo);

        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
        cityFrom.setTypeface(font);
        cityTo.setTypeface(font);
        countryTo.setTypeface(font);
        countryFrom.setTypeface(font);
        stateFrom.setTypeface(font);
        stateTo.setTypeface(font);

        search = (Button) v.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), SearchResult.class);
                ArrayList<String> params = new ArrayList<String>();
                params.add(cityFrom.getText().toString());
                params.add(cityTo.getText().toString());
                params.add(stateFrom.getText().toString());
                params.add(stateTo.getText().toString());
                params.add(countryFrom.getText().toString());
                params.add(countryTo.getText().toString());
               // params.add(date_time_converter() + "");
                intent.putStringArrayListExtra("PARAMS", params);
                InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            startActivity(intent);
                        } else {
                            LayoutInflater li = LayoutInflater.from(getActivity());
                            View v = li.inflate(R.layout.warning, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                            // set more_info.xml to alertdialog builder
                            alertDialogBuilder.setView(v);
                            TextView tittle = (TextView) v.findViewById(R.id.tittle);
                            TextView textView7 = (TextView) v.findViewById(R.id.textView7);
                            ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);
                            Typeface font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
                            tittle.setTypeface(font);
                            textView7.setTypeface(font);

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            close_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            // show it
                            alertDialog.show();
                        }
                        //  Toast.makeText(getActivity(), "No Internet Access..", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar = Calendar.getInstance();
    }

    protected String buildValueOf(int value) {
        if (value >= 10)
            return String.valueOf(value);
        else
            return "0" + String.valueOf(value);
    }

//    private long date_time_converter() {
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        long timeInMilliseconds = -1;
//        try {
//            Date mDate = mSimpleDateFormat.parse(date.getText().toString() + " " + time.getText().toString());
//            timeInMilliseconds = mDate.getTime();
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTimeInMillis(timeInMilliseconds);
//
//            Toast.makeText(getActivity().getApplicationContext(), timeInMilliseconds + "\n"
//                    + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + (calendar2.get(Calendar.MONTH) + 1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "\n"
//                    + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return timeInMilliseconds;
//    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
      //  time.setText(buildValueOf(hour) + ":" + buildValueOf(minute));
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
      //  date.setText(buildValueOf(day) + "/" + buildValueOf(month + 1) + "/" + year);
    }
}