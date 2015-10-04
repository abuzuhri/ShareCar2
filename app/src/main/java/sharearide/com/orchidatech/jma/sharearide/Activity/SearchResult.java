package sharearide.com.orchidatech.jma.sharearide.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;


public class SearchResult extends ActionBarActivity {
    private Toolbar tool_bar;
    private ImageView time, time2, time3, time4, date, date2, date3, date4, result_img, result_img2, result_img3, result_img4;
    private ProgressBar mProgressBar;
    ArrayList<Ride> rides;
    Map<Ride, User> ridesData;
    MyAdapter adapter;
    RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        mProgressBar = (ProgressBar) this.findViewById(R.id.search_progress);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        Intent intent = getIntent();
        ArrayList<String> params = intent.getStringArrayListExtra("PARAMS");

        if(params != null){
            findRide(params.get(0), params.get(1), params.get(2), params.get(3),
                    params.get(4), params.get(5), Long.parseLong(params.get(6))
            );
        }else{

        }


        /*
        result_img = (ImageView) findViewById(R.id.result_img);
        date = (ImageView) findViewById(R.id.date);
        time = (ImageView) findViewById(R.id.time);



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


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);

        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchResult.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(getApplicationContext(), Save_info.class);
                Bundle args = new Bundle();
                ArrayList<String> selected_ride_data = new ArrayList<>();
                selected_ride_data.add(selected_ride.getRemoteId()+"");
                selected_ride_data.add(selected_ride.getUserId()+"");
                selected_ride_data.add(selected_ride.getFromCity());
                selected_ride_data.add(selected_ride.getToCity());
                selected_ride_data.add(selected_ride.getFromState());
                selected_ride_data.add(selected_ride.getToState());
                selected_ride_data.add(selected_ride.getFromCountry());
                selected_ride_data.add(selected_ride.getToCountry());
                selected_ride_data.add(selected_ride.getDateTime()+"");
                selected_ride_data.add(selected_ride.getCost()+"");

                ArrayList<String> target_user_data = new ArrayList<>();
                target_user_data.add(target_user.getRemoteId()+"");
                target_user_data.add(target_user.getUsername());
                target_user_data.add(target_user.getPhone());
                target_user_data.add(target_user.getEmail());
                args.putStringArrayList("RIDE", selected_ride_data);
                args.putStringArrayList("USER", target_user_data);
                intent.putExtra("ARGS", args);
                //            Toast.makeText(activity.getApplicationContext(), ridesData.get(rides.get(position)).getUsername()+"", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);


    }

    private void findRide(final String city_from, final String city_to, final String state_from, final String state_to, final String country_from, final String country_to, final long date_time) {
        MainUserFunctions.find_a_ride(new OnSearchListener() {
                                          @Override
                                          public void onSearchSucceed(ArrayList<sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride> matchedRides, Map<sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride, User> matchedRidesData) {
                                              Toast.makeText(SearchResult.this, matchedRides.size() + ", " + matchedRidesData.size(), Toast.LENGTH_LONG).show();
                                              for(Ride ride : matchedRides) {
                                                  try {
                                                      RideDAO.addNewRide(ride);
                                                      UserDAO.addNewUser(matchedRidesData.get(ride));
                                                  } catch (EmptyFieldException e) {
                                                      e.printStackTrace();
                                                  } catch (InvalidInputException e) {
                                                      e.printStackTrace();
                                                  }
                                              }

                                              mProgressBar.setVisibility(View.GONE);
                                              rides.addAll(matchedRides);
                                              ridesData.putAll(matchedRidesData);
                                              adapter.notifyDataSetChanged();

//                                                     Toast.makeText(SearchResult.this.getApplicationContext(), matchedRidesData.get(matchedRides.get(0)).getUsername()+"" + matchedRidesData.size(), Toast.LENGTH_LONG).show();

                                          }

                                          @Override
                                          public void onSearchFailed(String error) {
                                              mProgressBar.setVisibility(View.GONE);
                                          }
                                      }, SearchResult.this.getApplicationContext(), city_from, city_to, state_from,
                state_to, country_from, country_to,
                date_time, getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1));



    }

}
