package sharearide.com.orchidatech.jma.sharearide.Fragment;

/**
 * Created by Amal on 9/17/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;

/**
 * Created by Edwin on 15/02/2015.
 */
public class FindRide extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText cityFrom, cityTo, countryFrom, countryTo, stateFrom, stateTo, time, date;
    private Button search;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_ride, container, false);

        cityFrom = (EditText) v.findViewById(R.id.cityFrom);
        cityTo = (EditText) v.findViewById(R.id.cityTo);
        countryFrom = (EditText) v.findViewById(R.id.countryFrom);
        countryTo = (EditText) v.findViewById(R.id.countryTo);
        stateFrom = (EditText) v.findViewById(R.id.stateFrom);
        stateTo = (EditText) v.findViewById(R.id.stateTo);
        time = (EditText) v.findViewById(R.id.time);
        date = (EditText) v.findViewById(R.id.date);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(FindRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    TimePickerDialog.newInstance(FindRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");

                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = DatePickerDialog.newInstance(FindRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setMinDate(calendar);
                dialog.setCancelable(false);
                dialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    DatePickerDialog dialog = DatePickerDialog.newInstance(FindRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.setMinDate(calendar);
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getFragmentManager(), "datePicker");
                }
            }
        });
        search = (Button) v.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            MainUserFunctions.find_a_ride(new OnSearchListener() {
                                                              @Override
                                                              public void onSearchSucceed(ArrayList<Ride> matchedRides, Map<Ride, User> matchedRidesData) {

                                                                  Toast.makeText(getActivity().getApplicationContext(), matchedRides.size() + ", " + matchedRidesData.size(), Toast.LENGTH_LONG).show();

                                                              }

                                                              @Override
                                                              public void onSearchFailed(String error) {
                                                              }
                                                          }, getActivity(), cityFrom.getText().toString(), cityTo.getText().toString(), stateFrom.getText().toString(),
                                    stateTo.getText().toString(), countryFrom.getText().toString(), countryTo.getText().toString(),
                                    date_time_converter());
                        } else
                            Toast.makeText(getActivity(), "No Internet Access..", Toast.LENGTH_LONG).show();
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

    private long date_time_converter() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        long timeInMilliseconds = -1;
        try {
            Date mDate = mSimpleDateFormat.parse(date.getText().toString() + " " + time.getText().toString());
            timeInMilliseconds = mDate.getTime();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(timeInMilliseconds);

            Toast.makeText(getActivity().getApplicationContext(), timeInMilliseconds + "\n"
                    + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + (calendar2.get(Calendar.MONTH) + 1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "\n"
                    + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        time.setText(buildValueOf(hour) + ":" + buildValueOf(minute));
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date.setText(buildValueOf(day) + "/" + buildValueOf(month + 1) + "/" + year);
    }
}