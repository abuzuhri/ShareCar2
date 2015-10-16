package sharearide.com.orchidatech.jma.sharearide.Fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class FindAllRide extends Fragment {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_ride, container, false);

        ed_search = (MaterialEditText) v.findViewById(R.id.ed_search);
        searchAll = (ImageButton) v.findViewById(R.id.search);
        mProgressBar = (ProgressBar) v.findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);
        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_regular.ttf");
//       ed_search.setTypeface(font);
        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_search.getText().toString())) {
                  //  ed_search.setError("Nothing to search for");
                } else {

                    InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                //mProgressDialog.show();
                                mProgressBar.setVisibility(View.VISIBLE);
                                findAllRide((ed_search.getText()).toString(), getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
                                //   Toast.makeText(getActivity(),"DONE",Toast.LENGTH_LONG).show();

                                rides = new ArrayList<>();
                                ridesData = new HashMap<>();
                                adapter = new MyAdapter(getActivity(), rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
                                    @Override
                                    public void onItemClicked(Ride selected_ride, User target_user) {
                                        Intent intent = new Intent(getActivity(), ReviewRide.class);
                                        intent.putExtra("ride_id", selected_ride.getRemoteId());
                                        intent.putExtra("user_id", target_user.getRemoteId());
                                        startActivity(intent);

                                    }
                                });
                                rv.setLayoutManager(llm);
                                rv.setAdapter(adapter);

                            } else {
                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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

        return v;
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
        }, getActivity(), s, user_id);


    }

}




