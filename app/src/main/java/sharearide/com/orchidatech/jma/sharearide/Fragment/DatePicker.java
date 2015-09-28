package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.android.datetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bahaa on 21/9/2015.
 */
public class DatePicker extends DatePickerDialog{
    private OnDateListener listener;
    public static DatePicker instance;
    private DateFormat dateFormat;
    public interface OnDateListener{
        public void onDateSet(int year, int month, int day);
    }
    private final int  MILLISECONDS_IN_DAY = 864000;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null && activity instanceof OnDateListener){
            listener = (OnDateListener) activity;
        }
    }

    public static DatePicker getInstance(){
        if(instance == null)
            instance = new DatePicker();
        return instance;
    }
    public void showDateDialog(){
        Calendar c = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        setMinDate(c);
        DatePickerDialog.newInstance(new OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                listener.onDateSet(year, (monthOfYear+1), dayOfMonth);
            }
        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
    }



/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();

        DatePickerDialog date_dialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                listener.onDateSet(year, (monthOfYear+1), dayOfMonth);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        date_dialog.setCancelable(false);
//        date_dialog.getDatePicker().setCalendarViewShown(false);
        date_dialog.getDatePicker().setMinDate(System.currentTimeMillis()-MILLISECONDS_IN_DAY);
        return date_dialog;
    }
  */


}
