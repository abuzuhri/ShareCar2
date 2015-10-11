package sharearide.com.orchidatech.jma.sharearide.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<Ride> orginal_rides;
    Map<Ride, User> ridesData;
    Map<Ride, User> orginal_ridesData;
    MyAdapter adapter;
    RecyclerView rv;
    private LinearLayoutManager llm;
    EditText t1;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        t1=(EditText)findViewById(R.id.ed_search);
        search=(ImageView)findViewById(R.id.search);
        mProgressBar = (ProgressBar) this.findViewById(R.id.search_progress);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ArrayList<String> params = intent.getStringArrayListExtra("PARAMS");

        if(params != null){
            findRide(params.get(0), params.get(1), params.get(2), params.get(3),
                    params.get(4), params.get(5), Long.parseLong(params.get(6))
            );
        }else{

        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocal(t1.getText().toString());
            }
        });


        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchLocal(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  searchLocal(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                // searchLocal(s.toString());
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);

        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        orginal_rides = new ArrayList<>();
        orginal_ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchResult.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(getApplicationContext(), ReviewRide.class);
//                Bundle args = new Bundle();
//                ArrayList<String> selected_ride_data = new ArrayList<>();
//                selected_ride_data.add(selected_ride.getRemoteId()+"");
//                selected_ride_data.add(selected_ride.getUserId()+"");
//                selected_ride_data.add(selected_ride.getFromCity());
//                selected_ride_data.add(selected_ride.getToCity());
//                selected_ride_data.add(selected_ride.getFromState());
//                selected_ride_data.add(selected_ride.getToState());
//                selected_ride_data.add(selected_ride.getFromCountry());
//                selected_ride_data.add(selected_ride.getToCountry());
//                selected_ride_data.add(selected_ride.getDateTime()+"");
//                selected_ride_data.add(selected_ride.getCost()+"");
//                selected_ride_data.add(selected_ride.getFrom_Lattitude()+"");
//                selected_ride_data.add(selected_ride.getFrom_Longitude()+"");
//                selected_ride_data.add(selected_ride.getTo_latitude()+"");
//                selected_ride_data.add(selected_ride.getTo_longitude()+"");
//
//                ArrayList<String> target_user_data = new ArrayList<>();
//                target_user_data.add(target_user.getRemoteId()+"");
//                target_user_data.add(target_user.getUsername());
//                target_user_data.add(target_user.getPhone());
//                target_user_data.add(target_user.getEmail());
//                args.putStringArrayList("RIDE", selected_ride_data);
//                args.putStringArrayList("USER", target_user_data);
//                intent.putExtra("ARGS", args);
                intent.putExtra("ride_id", selected_ride.getRemoteId());
                intent.putExtra("user_id", target_user.getRemoteId());
                Toast.makeText(SearchResult.this,""+selected_ride.getTo_longitude(),Toast.LENGTH_LONG).show();
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
                                              for (Ride ride : matchedRides) {
                                                  RideDAO.addNewRide(ride);
                                                  UserDAO.addNewUser(matchedRidesData.get(ride));

                                              }

                                              mProgressBar.setVisibility(View.GONE);
                                              rides.addAll(matchedRides);
                                              ridesData.putAll(matchedRidesData);
                                              orginal_rides.addAll(rides);
                                              orginal_ridesData.putAll(matchedRidesData);
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
    public void searchLocal(String s){
        rides.clear();
        ridesData.clear();
        for(Ride ride : orginal_rides){
            if(ride.getFromCity().indexOf(s)!=-1||ride.getFromCountry().indexOf(s)!=-1||ride.getToCity().indexOf(s)!=-1
                    ||ride.getFromCity().indexOf(s)!=-1||ride.getFromState().indexOf(s)!=-1||ride.getToState().indexOf(s)!=-1){
                rides.add(ride);
                ridesData.put(ride, orginal_ridesData.get(ride));
            }
            for(Ride _ride : orginal_rides){
                if(!rides.contains(_ride)){
                    if(orginal_ridesData.get(_ride).getUsername().indexOf(s)!=-1){
                        rides.add(_ride);
                        ridesData.put(_ride, orginal_ridesData.get(_ride));
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent =new Intent(SearchResult.this,ShareRide.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
