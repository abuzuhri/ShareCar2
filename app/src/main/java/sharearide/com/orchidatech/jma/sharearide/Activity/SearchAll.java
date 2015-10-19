package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;

/*
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.annotation.AnnotationFormatError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.ReviewRide;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;

public class SearchAll extends ActionBarActivity {
    private ImageButton searchAll;
    private MaterialEditText ed_search;
    MyAdapter adapter;
    RecyclerView rv;
    private ProgressBar mProgressBar;
    ArrayList<Ride> rides;
    ArrayList<Ride> orginal_rides;
    Map<Ride, User> ridesData;
    Map<Ride, User> orginal_ridesData;
    private LinearLayoutManager llm;
    Typeface font;

    @Nullable
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_all);

        ed_search = (MaterialEditText)findViewById(R.id.ed_search);
        searchAll = (ImageButton) findViewById(R.id.search);
        mProgressBar = (ProgressBar) findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
//       ed_search.setTypeface(font);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(SearchAll.this);
        rv.setLayoutManager(llm);
        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchAll.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(SearchAll.this, ReviewRide.class);
                intent.putExtra("ride_id", selected_ride.getRemoteId());
                intent.putExtra("user_id", target_user.getRemoteId());
                startActivity(intent);

            }
        });
        rv.setAdapter(adapter);

        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_search.getText().toString())) {
                    //  ed_search.setError("Nothing to search for");
                } else {

                    InternetConnectionChecker.isConnectedToInternet(SearchAll.this, new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                //mProgressDialog.show();
                                mProgressBar.setVisibility(View.VISIBLE);
                                findAllRide(ed_search.getText().toString(),SearchAll.this.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
                                //   Toast.makeText(getActivity(),"DONE",Toast.LENGTH_LONG).show();


                            } else {
                                LayoutInflater li = LayoutInflater.from(SearchAll.this);
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchAll.this);

                                // set more_info.xml to alertdialog builder
                                alertDialogBuilder.setView(v);
                                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                                ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

                                // create alert dialog
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                close_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                // show it
                                alertDialog.show();
                            }
                        }
                    });
                }

            }
        });


    }

    private void findAllRide(final String s, long user_id) {
        MainUserFunctions.find_all_ride(new OnSearchListener() {

            @Override
            public void onSearchSucceed(ArrayList<Ride> matchedRides, Map<Ride, User> matchedRidesData) {
                for (Ride ride : matchedRides) {

//                    RideDAO.addNewRide(ride);
                    RideDAO.addNewRide(ride.getRemoteId(), ride.getUserId(), ride.getFromCity(), ride.getToCity(), ride.getFromCountry(), ride.getToCountry(), ride.getFromState(), ride.getToState(),
                            ride.getDateTime(), ride.getCost(), ride.getMore_info(),ride.getFrom_Longitude(), ride.getTo_longitude(), ride.getFrom_Lattitude(), ride.getTo_latitude());
                    User user = matchedRidesData.get(ride);
                    UserDAO.addNewUser(user.getRemoteId(), user.getUsername(), user.getPassword(), user.getImage(), user.getAddress(), user.getBirthdate(), user.getGender(), user.getPhone(), user.getEmail());

                    //UserDAO.addNewUser(matchedRidesData.get(ride));

                }

                mProgressBar.setVisibility(View.GONE);
                rides.addAll(matchedRides);
                ridesData.putAll(matchedRidesData);
                adapter.notifyDataSetChanged();
//Toast.makeText(getActivity(), matchedRidesData.get(matchedRides.get(0)).getUsername()+"" + matchedRidesData.size(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSearchFailed(String error) {
                mProgressBar.setVisibility(View.GONE);
            }
        }, SearchAll.this, s, user_id);


    }

}



*/
public class SearchAll extends AppCompatActivity {
    Toolbar toolbar;
    private ImageButton searchAll;
    private MaterialEditText ed_search;
    MyAdapter adapter;
    RecyclerView rv;
    private ProgressBar mProgressBar;
    ArrayList<Ride> rides;
    ArrayList<Ride> orginal_rides;
    Map<Ride, User> ridesData;
    Map<Ride, User> orginal_ridesData;
    private LinearLayoutManager llm;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_all);

        toolbar = (Toolbar) findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ed_search = (MaterialEditText) findViewById(R.id.ed_search);
        searchAll = (ImageButton) findViewById(R.id.search);
        mProgressBar = (ProgressBar) findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");
//       ed_search.setTypeface(font);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(SearchAll.this);
        rv.setLayoutManager(llm);
        rides = new ArrayList<>();
        ridesData = new HashMap<>();
        adapter = new MyAdapter(SearchAll.this, rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride, User target_user) {
                Intent intent = new Intent(SearchAll.this, ReviewRide.class);
                intent.putExtra("ride_id", selected_ride.getRemoteId());
                intent.putExtra("user_id", target_user.getRemoteId());
                startActivity(intent);

            }
        });
        rv.setAdapter(adapter);

    }


    private void findAllRide(final String s, long user_id) {
        rides.clear();
        ridesData.clear();
        MainUserFunctions.find_all_ride(new OnSearchListener() {


            @Override
            public void onSearchSucceed(ArrayList<Ride> matchedRides, Map<Ride, User> matchedRidesData, int count) {

                    for (Ride ride : matchedRides) {


//                    RideDAO.addNewRide(ride);
                        RideDAO.addNewRide(ride.getRemoteId(), ride.getUserId(), ride.getFromCity(), ride.getToCity(), ride.getFromCountry(), ride.getToCountry(), ride.getFromState(), ride.getToState(),
                                ride.getDateTime(), ride.getCost(), ride.getMore_info(), ride.getFrom_Longitude(), ride.getTo_longitude(), ride.getFrom_Lattitude(), ride.getTo_latitude());
                        User user = matchedRidesData.get(ride);
                        UserDAO.addNewUser(user.getRemoteId(), user.getUsername(), user.getPassword(), user.getImage(), user.getAddress(), user.getBirthdate(), user.getGender(), user.getPhone(), user.getEmail());

                        //UserDAO.addNewUser(matchedRidesData.get(ride));

                    }

                    mProgressBar.setVisibility(View.GONE);
                    rides.addAll(matchedRides);
                    ridesData.putAll(matchedRidesData);
                    adapter.notifyDataSetChanged();
//Toast.makeText(getActivity(), matchedRidesData.get(matchedRides.get(0)).getUsername()+"" + matchedRidesData.size(), Toast.LENGTH_LONG).show();

                }

            @Override
            public void onSearchFailed(String error) {
                mProgressBar.setVisibility(View.GONE);

            }

        }, SearchAll.this, s, user_id);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_all, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent =new Intent(SearchAll.this,ShareRide.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            ed_search.setTypeface(font);
            if (TextUtils.isEmpty(ed_search.getText().toString())) {
                  ed_search.setError("Nothing to search for");
            } else {

                InternetConnectionChecker.isConnectedToInternet(SearchAll.this, new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            //mProgressDialog.show();
                            mProgressBar.setVisibility(View.VISIBLE);
                            findAllRide(ed_search.getText().toString(), SearchAll.this.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
                            //   Toast.makeText(getActivity(),"DONE",Toast.LENGTH_LONG).show();


                        } else {
                            LayoutInflater li = LayoutInflater.from(SearchAll.this);
                            View v = li.inflate(R.layout.warning, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchAll.this);

                            // set more_info.xml to alertdialog builder
                            alertDialogBuilder.setView(v);
                            TextView tittle = (TextView) v.findViewById(R.id.tittle);
                            ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            close_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            // show it
                            alertDialog.show();
                        }
                    }
                });
            }
            // searchLocal(t1.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
