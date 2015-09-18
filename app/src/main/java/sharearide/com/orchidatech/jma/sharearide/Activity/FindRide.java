package sharearide.com.orchidatech.jma.sharearide.Activity;

/**
 * Created by Amal on 9/17/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sharearide.com.orchidatech.jma.sharearide.R;


public class FindRide extends Fragment {
private Button save,more_info;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.find_ride,container,false);
        save=(Button)v.findViewById(R.id.save);
        more_info=(Button)v.findViewById(R.id.more_info);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,Save_info.class);
                startActivity(i);
            }
        });

        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i = new Intent(context, Save_info.class);
               // startActivity(i);
            }
        });
        return v;
    }
}