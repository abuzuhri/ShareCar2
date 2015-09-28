package sharearide.com.orchidatech.jma.sharearide.Activity;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.SearchResultAdapter;


public class SearchResult extends ActionBarActivity {
    private Toolbar tool_bar;
    private ImageView time, time2, time3, time4, date, date2, date3, date4, result_img, result_img2, result_img3, result_img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);

        /*
        result_img = (ImageView) findViewById(R.id.result_img);
        date = (ImageView) findViewById(R.id.date);
        time = (ImageView) findViewById(R.id.time);

        result_img2 = (ImageView) findViewById(R.id.result_img2);
        date2 = (ImageView) findViewById(R.id.date2);
        time2 = (ImageView) findViewById(R.id.time2);

        result_img3 = (ImageView) findViewById(R.id.result_img3);
        date3 = (ImageView) findViewById(R.id.date3);
        time3 = (ImageView) findViewById(R.id.time3);

        result_img4 = (ImageView) findViewById(R.id.result_img4);
        date4 = (ImageView) findViewById(R.id.date4);
        time4 = (ImageView) findViewById(R.id.time4);


        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        result_img.getLayoutParams().height = (int) (height * 0.09);
        result_img.getLayoutParams().width = (int) (width * 0.09);

        date.getLayoutParams().height = (int) (height * 0.04);
        date.getLayoutParams().width = (int) (width * 0.05);

        time.getLayoutParams().height = (int) (height * 0.04);
        time.getLayoutParams().width = (int) (width * 0.05);

        result_img2.getLayoutParams().height = (int) (height * 0.09);
        result_img2.getLayoutParams().width = (int) (width * 0.09);

        date2.getLayoutParams().height = (int) (height * 0.04);
        date2.getLayoutParams().width = (int) (width * 0.05);

        time2.getLayoutParams().height = (int) (height * 0.04);
        time2.getLayoutParams().width = (int) (width * 0.05);

        result_img3.getLayoutParams().height = (int) (height * 0.09);
        result_img3.getLayoutParams().width = (int) (width * 0.09);

        date3.getLayoutParams().height = (int) (height * 0.04);
        date3.getLayoutParams().width = (int) (width * 0.05);

        time3.getLayoutParams().height = (int) (height * 0.04);
        time3.getLayoutParams().width = (int) (width * 0.05);

        result_img4.getLayoutParams().height = (int) (height * 0.09);
        result_img4.getLayoutParams().width = (int) (width * 0.09);

        date4.getLayoutParams().height = (int) (height * 0.04);
        date4.getLayoutParams().width = (int) (width * 0.05);

        time4.getLayoutParams().height = (int) (height * 0.04);
        time4.getLayoutParams().width = (int) (width * 0.05);
         */


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setLayoutManager(llm);

        Ride ride = new Ride();
        List<Ride> rides = ride.initializeData();
        //List<Ride> rides = RideDAO.getAllRides();

        SearchResultAdapter adapter = new SearchResultAdapter(rides);
        rv.setAdapter(adapter);

    }

}
