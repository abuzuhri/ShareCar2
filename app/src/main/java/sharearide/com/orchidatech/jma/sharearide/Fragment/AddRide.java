package sharearide.com.orchidatech.jma.sharearide.Fragment;

/**
 * Created by Amal on 9/17/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;

public class AddRide extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
private Button save,more_info;
    private Context context;
    private EditText cityFrom,cityTo,countryFrom,countryTo,stateFrom,stateTo,time,date;
    private EditText price;
    private Calendar calendar;



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

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(AddRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");
                time.setInputType(InputType.TYPE_NULL);

            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    TimePickerDialog.newInstance(AddRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DatePicker dialog = new DatePicker();
                dialog.show(getActivity().getFragmentManager(), "Date Dialog");*/
                DatePickerDialog dialog = DatePickerDialog.newInstance(AddRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setMinDate(calendar);
                dialog.setCancelable(true);
                dialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
//                    DatePicker dialog = DatePicker.getInstance();
//                    dialog.showDateDialog();
                    DatePickerDialog dialog = DatePickerDialog.newInstance(AddRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.setMinDate(calendar);
                    dialog.setCancelable(true);
                    dialog.show(getActivity().getFragmentManager(), "datePicker");

                }
            }
        });

        price=(EditText)v.findViewById(R.id.price);
        context = getActivity();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityFrom.getText().toString().equals("")) {
                    cityFrom.setError("Required Field");
                } else if (countryFrom.getText().toString().equals(""))
                    countryFrom.setError("Required Field");
                else if (time.getText().toString().equals(""))
                  time.setError("Required Field ");
              else if (date.getText().toString().equals(""))
                   date.setError("Required Field ");
                else if (cityTo.getText().toString().equals(""))
                    cityTo.setError("Required Field ");
                else if (countryTo.getText().toString().equals(""))
                    countryTo.setError("Required Field ");
                else{
                    offerRide(context.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1),
                            cityFrom.getText().toString(), cityTo.getText().toString(),
                            stateFrom.getText().toString(), stateTo.getText().toString(),
                            countryFrom.getText().toString(), countryTo.getText().toString(),
                            date_time_converter(), Double.parseDouble(price.getText().toString())
                            );

            }}
        });

        more_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                View v = li.inflate(R.layout.more_info, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set more_info.xml to alertdialog builder
                alertDialogBuilder.setView(v);
                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                ImageButton confirm_btn = (ImageButton) v.findViewById(R.id.confirm_btn);
                EditText info = (EditText) v.findViewById(R.id.info);

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                // show it
                alertDialog.show();
}
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar = Calendar.getInstance();
    }

    private long date_time_converter() {
        SimpleDateFormat mSimpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        long timeInMilliseconds = -1;
        try {
            Date mDate = mSimpleDateFormat.parse(date.getText().toString() + " " + time.getText().toString());
            timeInMilliseconds = mDate.getTime();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(timeInMilliseconds);

            Toast.makeText(getActivity().getApplicationContext(), timeInMilliseconds+"\n"
                    + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + (calendar2.get(Calendar.MONTH)+1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "\n"
                    + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
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
    protected String buildValueOf(int value) {
        if(value>=10)
            return String.valueOf(value);
        else
            return "0" + String.valueOf(value);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        time.setText(buildValueOf(hour) + ":" + buildValueOf(minute));
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date.setText(buildValueOf(day) + "/" + buildValueOf(month+1) + "/" + year);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        // or
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}