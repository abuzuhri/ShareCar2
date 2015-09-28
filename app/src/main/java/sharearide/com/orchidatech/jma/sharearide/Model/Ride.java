package sharearide.com.orchidatech.jma.sharearide.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shadow on 9/22/2015.
 */
public class Ride {

    public String name;
    public String time;
    public String date;

    public Ride() {
    }

    public Ride(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }


    public List<Ride> initializeData() {
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Shareef", "12:00", "22/9/2015"));
        rides.add(new Ride("Bahaa", "12:00", "22/9/2015"));
        rides.add(new Ride("Amal", "12:00", "22/9/2015"));

        return rides;
    }

    /*
    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
    */

}
