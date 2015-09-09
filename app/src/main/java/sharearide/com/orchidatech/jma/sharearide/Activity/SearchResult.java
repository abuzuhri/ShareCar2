package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Shadow on 8/31/2015.
 */
public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.search_result);

        //List<Ride> rides = RideDAO.getAllRides();

        Intent intent = new Intent();
        // TODO: get all rides
        //List<Ride> rides = intent.getParcelableArrayListExtra("rides");

        /*
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        */
    }

}
