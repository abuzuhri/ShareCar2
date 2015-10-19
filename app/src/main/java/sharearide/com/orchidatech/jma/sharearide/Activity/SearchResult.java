package sharearide.com.orchidatech.jma.sharearide.Activity;


import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;


public class SearchResult extends ActionBarActivity {
    private Toolbar tool_bar;
    private ProgressBar mProgressBar;
    ArrayList<Ride> rides;
    ArrayList<Ride> orginal_rides;
    private LinearLayout search_bar;
    Map<Ride, User> ridesData;
    Map<Ride, User> orginal_ridesData;
    MyAdapter adapter;
    RecyclerView rv;
    private LinearLayoutManager llm;
    EditText ed_search;
    private FloatingActionButton load_more;
    ImageView search;
    ArrayList<String> params;
    long last_id_server = -1;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        Intent intent = getIntent();
        params = intent.getStringArrayListExtra("PARAMS");
//        getLastRecord();


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);

        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        orginal_rides = new ArrayList<>();
        orginal_ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchResult.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(getApplicationContext(), ReviewRide.class);
                intent.putExtra("ride_id", selected_ride.getRemoteId());
                intent.putExtra("user_id", target_user.getRemoteId());
                startActivity(intent);
            }
        });
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        ed_search=(EditText)findViewById(R.id.ed_search);
        search_bar=(LinearLayout)findViewById(R.id.search_bar);
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");
//        ed_search.setTypeface(font);

        // search=(ImageView)findViewById(R.id.search);
        mProgressBar = (ProgressBar) this.findViewById(R.id.search_progress);
        tool_bar = (Toolbar) findViewById(R.id.toolbar_2); // Attaching the layout to the toolbar object

        load_more = (FloatingActionButton) findViewById(R.id.btn_load_more);
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findRide(params.get(0), params.get(1), params.get(2), params.get(3),
                        params.get(4), params.get(5));
            }
        });

        tool_bar = (Toolbar) findViewById(R.id.toolbar_2); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(params != null){
            findRide(params.get(0), params.get(1), params.get(2), params.get(3),
                    params.get(4), params.get(5));
        }else{

        }
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchLocal(t1.getText().toString());
//            }
//        });


        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchLocal(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);

        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        orginal_rides = new ArrayList<>();
        orginal_ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchResult.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(getApplicationContext(), ReviewRide.class);
                intent.putExtra("ride_id", selected_ride.getRemoteId());
                intent.putExtra("user_id", target_user.getRemoteId());
                startActivity(intent);
            }
        });
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }
    //
//    private void getLastRecord() {
//
//        MainUserFunctions.get_last_record(this, getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1), new OnLoadFinished() {
//            @Override
//            public void onSuccess(JSONObject jsonObject) throws JSONException {
//                boolean success = jsonObject.getBoolean("success");
//                if (success) {
//                    last_id_server = Long.parseLong(jsonObject.getString("id"));
//                    rides_count_in_server = jsonObject.getInt("count");
//                    Toast.makeText(getApplicationContext(), last_id_server+", "+rides_count_in_server, Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                getLastRecord();
//            }
//        });
//    }
/**/
    private void findRide(final String city_from, final String city_to, final String state_from, final String state_to, final String country_from, final String country_to) {



        mProgressBar.setVisibility(View.VISIBLE);
        load_more.setVisibility(View.GONE);
        MainUserFunctions.find_a_ride(new OnSearchListener() {
                                          @Override
                                          public void onSearchSucceed(ArrayList<Ride> matchedRides, Map<Ride, User> matchedRidesData, int count, long last_id) {
                                              for (Ride ride : matchedRides) {
                                                  RideDAO.addNewRide(ride.getRemoteId(), ride.getUserId(), ride.getFromCity(), ride.getToCity(), ride.getFromCountry(), ride.getToCountry(), ride.getFromState(), ride.getToState(),
                                                          ride.getDateTime(), ride.getCost(), ride.getMore_info(),ride.getFrom_Longitude(), ride.getTo_longitude(), ride.getFrom_Lattitude(), ride.getTo_latitude());
                                                  User user = matchedRidesData.get(ride);
                                                  UserDAO.addNewUser(user.getRemoteId(), user.getUsername(), user.getPassword(), user.getImage(), user.getAddress(), user.getBirthdate(), user.getGender(), user.getPhone(), user.getEmail());
//                                                  UserDAO.addNewUser(matchedRidesData.get(ride));


                                              }


                                              rides.addAll(matchedRides);
                                              ridesData.putAll(matchedRidesData);
                                              orginal_rides.addAll(rides);
                                              orginal_ridesData.putAll(matchedRidesData);
                                              adapter.notifyDataSetChanged();
                                              mProgressBar.setVisibility(View.GONE);


                                                  load_more.setVisibility(count == -1?View.GONE:View.VISIBLE);
                                                   last_id_server =last_id_server == -1?last_id:last_id_server;


//                                                     Toast.makeText(SearchResult.this.getApplicationContext(), matchedRidesData.get(matchedRides.get(0)).getUsername()+"" + matchedRidesData.size(), Toast.LENGTH_LONG).show();

                                          }

                                          @Override
                                          public void onSearchFailed(String error) {
                                              mProgressBar.setVisibility(View.GONE);
                                              load_more.setVisibility(View.VISIBLE);
                                          }
                                      }, SearchResult.this.getApplicationContext(), city_from, city_to, state_from,
                state_to, country_from, country_to,
                getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1), llm.getItemCount(), last_id_server);



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

        if (id == R.id.action_search) {
            ed_search.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("");
            ed_search.setTypeface(font);

            // searchLocal(t1.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
