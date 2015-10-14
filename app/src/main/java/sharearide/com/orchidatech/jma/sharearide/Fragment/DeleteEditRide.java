package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import sharearide.com.orchidatech.jma.sharearide.Activity.AddLocation;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnAddressFetched;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnUpdateRideListener;

/**
 * Created by Bahaa on 7/10/2015.
 */
public class DeleteEditRide extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private ArrayList<EditText> allEditText;

    EditText cityFrom;
    EditText countryFrom;
    EditText stateFrom;
    EditText time;
    EditText date;
    EditText cityTo;
    EditText countryTo;
    EditText stateTo;
    EditText price;
    EditText info;
    ImageButton confirm_btn,close_btn;
    Button more_info;
    Button save;
    Ride ride;
    Ride origin_ride;
    private  ImageButton firstlocation_lat_long,secondlocation_lat_long;
    private String from_Lattitude,from_Longitude,to_Lattitude,to_Longitude;
    private Calendar calendar;
     AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_edit_ride_fragment, null, false);
        setHasOptionsMenu(true);

        ride = RideDAO.getRideByRemoteId(getArguments().getLong("id", -1));
        origin_ride = ride;
        allEditText = new ArrayList<>();
        cityFrom = (EditText) view.findViewById(R.id.cityFrom);
        allEditText.add(cityFrom);
        countryFrom = (EditText) view.findViewById(R.id.countryFrom);
        allEditText.add(countryFrom);
        stateFrom = (EditText) view.findViewById(R.id.stateFrom);
        allEditText.add(stateFrom);
        time = (EditText) view.findViewById(R.id.time);
        allEditText.add(time);
        date = (EditText) view.findViewById(R.id.date);
        allEditText.add(date);
        cityTo = (EditText) view.findViewById(R.id.cityTo);
        allEditText.add(cityTo);
        countryTo = (EditText) view.findViewById(R.id.countryTo);
        allEditText.add(countryTo);
        stateTo = (EditText) view.findViewById(R.id.stateTo);
        allEditText.add(stateTo);
        price = (EditText) view.findViewById(R.id.price);
        allEditText.add(price);
        more_info = (Button) view.findViewById(R.id.more_info);
        save = (Button) view.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if(!ifChangeOccurred()) {
//                        Toast.makeText(getActivity(), "No Changes Found..", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Updating...");
                pd.setCancelable(false);

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
                else {
                    pd.show();

                    MainUserFunctions.update_ride(getActivity(), ride.getRemoteId(), cityFrom.getText().toString(), cityTo.getText().toString(),
                            stateFrom.getText().toString(), stateTo.getText().toString(), countryFrom.getText().toString(),
                            countryTo.getText().toString(), date_time_converter(), price.getText().toString(), from_Lattitude, from_Longitude,
                            to_Lattitude, to_Longitude, ride.getMore_info(), new OnUpdateRideListener() {
                                @Override
                                public void onFinished(Ride ride) {

                                    pd.dismiss();

//                                RideDAO.deleteRide(ride.getRemoteId());
//
//                                RideDAO.addNewRide(ride);

//                            getFragmentManager().beginTransaction().remove(DeleteEditRide.this).commit();
                                    getFragmentManager().popBackStack();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_place, new MyRides()).commit();
                                    getFragmentManager().executePendingTransactions();
                                }

                                @Override
                                public void onFailed(String error) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View dialog_view = li.inflate(R.layout.more_info, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        firstlocation_lat_long=(ImageButton)view.findViewById(R.id.firstlocation_lat_long);
        secondlocation_lat_long=(ImageButton)view.findViewById(R.id.secondlocation_lat_long);
        firstlocation_lat_long.setEnabled(false);
        secondlocation_lat_long.setEnabled(false);
        alertDialogBuilder.setView(dialog_view);
          confirm_btn = (ImageButton) dialog_view.findViewById(R.id.confirm_btn);
          close_btn = (ImageButton) dialog_view.findViewById(R.id.close_btn);
        info = (EditText) dialog_view.findViewById(R.id.info);
        info.setEnabled(false);
        allEditText.add(info);

         alertDialog = alertDialogBuilder.create();

        more_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                confirm_btn.setVisibility(View.GONE);
                close_btn.setVisibility(View.VISIBLE);

                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ride.more_info = info.getText().toString();

                        alertDialog.dismiss();

                    }
                });
                info.setText(ride.more_info);
                info.setEnabled(false);
                alertDialog.show();

            }
        });
        fillFields();
        return view;

    }

    private void fillFields() {
        cityFrom.setText(ride.getFromCity());
        cityTo.setText(ride.toCity);
        countryFrom.setText(ride.getFromCountry());
        countryTo.setText(ride.getToCountry());
        stateFrom.setText(ride.getFromState());
        stateTo.setText(ride.getToState());
        long date_time = ride.getDateTime();
        String fullDate = millToDate(date_time);
        time.setText(fullDate.split(" ")[1]);
        date.setText(fullDate.split(" ")[0]);
        price.setText(ride.getCost());

        firstlocation_lat_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddLocation.class);
                intent.putExtra("Request",100);
                startActivityForResult(intent, 100);
            }
        });
        secondlocation_lat_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddLocation.class);
                intent.putExtra("Request",101);
                startActivityForResult(intent, 101);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(DeleteEditRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");

            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    TimePickerDialog.newInstance(DeleteEditRide.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "timePicker");
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DatePicker dialog = new DatePicker();
                dialog.show(getActivity().getFragmentManager(), "Date Dialog");*/
                DatePickerDialog dialog = DatePickerDialog.newInstance(DeleteEditRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setMinDate(calendar);
                dialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
//                    DatePicker dialog = DatePicker.getInstance();
//                    dialog.showDateDialog();
                    DatePickerDialog dialog = DatePickerDialog.newInstance(DeleteEditRide.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.setMinDate(calendar);
                    dialog.show(getActivity().getFragmentManager(), "datePicker");
                }
            }
        });



    }


    private String millToDate(long timeInMilliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliSeconds);

        return buildValueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + buildValueOf((calendar.get(Calendar.MONTH)+1)) + "/" + buildValueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                buildValueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + buildValueOf(calendar.get(Calendar.MINUTE));

    }

    protected String buildValueOf(int value) {
        if(value>=10)
            return String.valueOf(value);
        else
            return "0" + String.valueOf(value);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_ride_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.edit_ride:
                for(EditText editText : allEditText)
                    editText.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                firstlocation_lat_long.setEnabled(true);
                secondlocation_lat_long.setEnabled(true);

                more_info.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        close_btn.setVisibility(View.GONE);
                        confirm_btn.setVisibility(View.VISIBLE);
                        confirm_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ride.more_info = info.getText().toString();

                                alertDialog.dismiss();

                            }
                        });
                        info.setText(ride.more_info);
                        alertDialog.show();

                    }
                });

                break;
            case R.id.delete_ride:
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setCancelable(true).setMessage("Ride will be deleted, continue?").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog pd = new ProgressDialog(getActivity());
                                pd.setMessage("Deleting...");
                                pd.show();
                                MainUserFunctions.deleteRide(getActivity(), ride.getRemoteId(), new OnRequestFinished() {
                                    @Override
                                    public void onFinished(String s) {
                                        if (pd.isShowing())
                                            pd.dismiss();
                                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                                        getFragmentManager().beginTransaction().remove(DeleteEditRide.this).commit();
                                        getFragmentManager().popBackStack();
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_place, new MyRides()).commit();
                                        getFragmentManager().executePendingTransactions();
                                    }

                                    @Override
                                    public void onFailed() {
                                        if (pd.isShowing())
                                            pd.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("Attention");
                builder.create().show();
                break;
        }
        return true;
    }

    private long date_time_converter() {
        SimpleDateFormat mSimpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        long timeInMilliseconds = -1;
        try {
            Date mDate = mSimpleDateFormat.parse(date.getText().toString() + " " + time.getText().toString());
            timeInMilliseconds = mDate.getTime();
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTimeInMillis(timeInMilliseconds);
//
//            Toast.makeText(getActivity().getApplicationContext(), timeInMilliseconds+"\n"
//                    + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + (calendar2.get(Calendar.MONTH)+1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "\n"
//                    + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date.setText(buildValueOf(day) + "/" + buildValueOf(month + 1) + "/" + year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        time.setText(buildValueOf(hour) + ":" + buildValueOf(minute));

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

            Toast.makeText(getActivity(),data.getStringExtra("From_Lattitude")+" , "+data.getStringExtra("From_Longittude"),Toast.LENGTH_LONG).show();
        }else  if(resultCode==101){
            to_Lattitude=data.getStringExtra("From_Lattitude");
            to_Longitude=data.getStringExtra("From_Longittude");
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
            Toast.makeText(getActivity(),data.getStringExtra("From_Lattitude")+" , "+data.getStringExtra("From_Longittude"),Toast.LENGTH_LONG).show();
        }

    }
    private  boolean ifChangeOccurred(){
        if(cityFrom.getText().toString().equals(origin_ride.getFromCity())
                && cityTo.getText().toString().equals(origin_ride.getToCity())
                && countryFrom.getText().toString().equals(origin_ride.getFromCountry())
                && countryTo.getText().toString().equals(origin_ride.getToCountry())
                && stateFrom.getText().toString().equals(origin_ride.getFromState())
                && stateTo.getText().toString().equals(origin_ride.getToState())

                )
            return false;
        return true;

    }
}
