package sharearide.com.orchidatech.jma.sharearide.Fragment;

/**
 * Created by Amal on 9/17/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.AddLocation;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnAddressFetched;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestListener;

public class AddRide extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private Button save,more_info;
    private Context context;
    Typeface font;
    private EditText cityFrom,cityTo,countryFrom,countryTo,stateFrom,stateTo,time,date;
    private EditText price;
    private Calendar calendar;
    private  ImageButton firstlocation_lat_long,secondlocation_lat_long;
    private String from_Lattitude,from_Longitude,to_Lattitude,to_Longitude;
    private EditText info;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.add_ride,container,false);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);

        save=(Button)v.findViewById(R.id.save);
        firstlocation_lat_long=(ImageButton)v.findViewById(R.id.firstlocation_lat_long);
        secondlocation_lat_long=(ImageButton)v.findViewById(R.id.secondlocation_lat_long);
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

        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_regular.ttf");
        cityFrom.setTypeface(font);
        cityTo.setTypeface(font);
        countryTo.setTypeface(font);
        countryFrom.setTypeface(font);
        date.setTypeface(font);
        time.setTypeface(font);
        stateFrom.setTypeface(font);
        stateTo.setTypeface(font);
        price.setTypeface(font);
        firstlocation_lat_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            Intent intent = new Intent(getActivity(), AddLocation.class);
                            intent.putExtra("Request", 100);
                            startActivityForResult(intent, 100);
                        } else {
                            {

                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                                // set more_info.xml to alertdialog builder
                                alertDialogBuilder.setView(v);
                                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                                ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

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
                        }
                    }
                });

            }
        });
        secondlocation_lat_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            Intent intent = new Intent(getActivity(), AddLocation.class);
                            intent.putExtra("Request", 101);
                            startActivityForResult(intent, 101);
                        } else {
                            {

                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                                // set more_info.xml to alertdialog builder
                                alertDialogBuilder.setView(v);
                                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                                ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

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
                        }
                    }
                });
            }
        });
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

        context = getActivity();
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.more_info, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set more_info.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);
        TextView tittle = (TextView) dialogView.findViewById(R.id.tittle);
        final ImageButton confirm_btn = (ImageButton) dialogView.findViewById(R.id.confirm_btn);
        final ImageButton close_btn = (ImageButton) dialogView.findViewById(R.id.close_btn);
        close_btn.setVisibility(View.GONE);
        info = (EditText) dialogView.findViewById(R.id.info);
        info.setTypeface(font);
        final AlertDialog alertDialog = alertDialogBuilder.create();

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
                else if(from_Lattitude!=null & to_Lattitude!=null){
                    InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                progressBar.setVisibility(View.VISIBLE);

                                offerRide(context.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1),
                                        cityFrom.getText().toString(), cityTo.getText().toString(),
                                        stateFrom.getText().toString(), stateTo.getText().toString(),
                                        countryFrom.getText().toString(), countryTo.getText().toString(),
                                        date_time_converter(), price.getText().toString(),
                                        info.getText().toString(), from_Lattitude, from_Longitude, to_Lattitude, to_Longitude
                                        , new OnRequestListener() {
                                            @Override
                                            public void onFinished() {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                            } else {

                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                                // set more_info.xml to alertdialog builder
                                alertDialogBuilder.setView(v);
                                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                                ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

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
                        }
                    });
                }
                else
                    Toast.makeText(getActivity(),"Please determine your Location",Toast.LENGTH_LONG).show();}
        });



        // create alert dialog
        more_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

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

//            Toast.makeText(getActivity().getApplicationContext(), timeInMilliseconds+"\n"
//                    + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + (calendar2.get(Calendar.MONTH)+1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "\n"
//                    + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public void offerRide( final long user_id,  final String city_from,  final String city_to,
                           final String state_from,  final String state_to,
                           final String country_from,  final String country_to,  final long date_time,
                           final String price, final String more_info, final String from_latitude,final String from_longitude,
                           final String to_latitude,final String to_longitude,final OnRequestListener listener)
    {
        InternetConnectionChecker.isConnectedToInternet(context, new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
                    MainUserFunctions.offerRide(context, user_id, city_from, city_to, state_from, state_to, country_from,
                            country_to, date_time, price, more_info, from_latitude, from_longitude, to_latitude, to_longitude, new OnRequestFinished() {
                                @Override
                                public void onFinished(String message) {
                                    progressBar.setVisibility(View.GONE);
                                    // context.startActivity(new Intent(context, MyRides.class));
                                    MyRides myRidesFragment = new MyRides();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_place, myRidesFragment).addToBackStack(null).commit();
                                    getFragmentManager().executePendingTransactions();
                                    // toolbar.setTitle("My Rides");

                                }

                                @Override
                                public void onFailed() {

                                }
                            });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View v = li.inflate(R.layout.warning, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    // set more_info.xml to alertdialog builder
                    alertDialogBuilder.setView(v);
                    TextView tittle = (TextView) v.findViewById(R.id.tittle);
                    ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

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

                // Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100){
            from_Lattitude=data.getStringExtra("From_Lattitude") ;
            from_Longitude=data.getStringExtra("From_Longittude");

//            final String requestString = "http://nominatim.openstreetmap.org/reverse?format=json&lat=" +
//                    from_Lattitude + "&lon=" + from_Longitude + "&zoom=18&addressdetails=1";
            MainUserFunctions.get_address(getActivity(), new OnAddressFetched() {
                @Override
                public void onFetched(Map<String, String> data) {
                    countryFrom.setText(data.get("country"));
                    stateFrom.setText(data.get("state"));
                    if (data.get("road") != null)
                        cityFrom.setText(data.get("road"));
                    else if (data.get("city") != null)
                        cityFrom.setText(data.get("city"));
                    else if (data.get("village") != null)
                        cityFrom.setText(data.get("village"));
                    else
                        cityFrom.setText(data.get("state"));
                }

                @Override
                public void onFailed(String error) {

                }
            }, data.getStringExtra("From_Longittude"), data.getStringExtra("From_Lattitude"), "json");

            //   Toast.makeText(getActivity(),data.getStringExtra("From_Lattitude")+" , "+data.getStringExtra("From_Longittude"),Toast.LENGTH_LONG).show();
        }else  if(resultCode==101){
            to_Lattitude=data.getStringExtra("From_Lattitude");
            to_Longitude=data.getStringExtra("From_Longittude");
            MainUserFunctions.get_address(getActivity(), new OnAddressFetched() {
                @Override
                public void onFetched(Map<String, String> data) {
                    countryTo.setText(data.get("country"));
                    stateTo.setText(data.get("state"));
                    if (data.get("road") != null)
                        cityTo.setText(data.get("road"));
                    else if (data.get("city") != null)
                        cityTo.setText(data.get("city"));
                    else if (data.get("village") != null)
                        cityTo.setText(data.get("village"));
                    else
                        cityTo.setText(data.get("state"));
                }

                @Override
                public void onFailed(String error) {

                }
            }, data.getStringExtra("From_Longittude"), data.getStringExtra("From_Lattitude"), "json");
            //  Toast.makeText(getActivity(),data.getStringExtra("From_Lattitude")+" , "+data.getStringExtra("From_Longittude"),Toast.LENGTH_LONG).show();
        }

    }

}