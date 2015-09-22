package sharearide.com.orchidatech.jma.sharearide.Activity;

/**
 * Created by Amal on 9/17/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Edwin on 15/02/2015.
 */
public class FindRide extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.find_ride,container,false);
        return v;
    }
}