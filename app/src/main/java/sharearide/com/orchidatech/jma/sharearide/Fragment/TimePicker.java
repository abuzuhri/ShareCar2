package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Bahaa on 21/9/2015.
 */
public class TimePicker extends DialogFragment {
    public interface OnTimeListener{
        public void onTimeSet(int hour, int minute);
    }
    private OnTimeListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null && activity instanceof OnTimeListener){
            listener = (OnTimeListener) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();

        TimePickerDialog time_dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                    listener.onTimeSet(hour, minute);
            }

        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MONTH), false);
        return time_dialog;
    }
}
