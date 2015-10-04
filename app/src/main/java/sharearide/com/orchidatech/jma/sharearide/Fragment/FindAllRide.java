package sharearide.com.orchidatech.jma.sharearide.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.*;
import sharearide.com.orchidatech.jma.sharearide.Activity.Save_info;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;
import sharearide.com.orchidatech.jma.sharearide.Utility.InvalidInputException;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSearchListener;

public class FindAllRide extends Fragment {
 private ImageButton searchAll;
 private EditText t1;
    MyAdapter adapter;
    RecyclerView rv;
    private ProgressBar mProgressBar;
    ArrayList<Ride> rides;
    Map<Ride, User> ridesData;
    private LinearLayoutManager llm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_ride, container, false);

     t1 = (EditText) v.findViewById(R.id.ed_search);
    searchAll = (ImageButton) v.findViewById(R.id.search);
    mProgressBar = (ProgressBar) v.findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);

        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        //  llm = new LinearLayoutManager(getActivity());
         llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
      searchAll.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
              //  if (getArguments().getString("message") != null) {
               if ((t1.getText()).toString() != null) {
                   mProgressBar.setVisibility(View.VISIBLE);

                    findAllRide((t1.getText()).toString(), getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1) );
              //   Toast.makeText(getActivity(),"DONE",Toast.LENGTH_LONG).show();


                }



                rides = new ArrayList<>();
                ridesData = new HashMap<>();
                adapter = new MyAdapter(getActivity(), rides, ridesData, new MyAdapter.OnRecycleViewItemClicked() {
                    @Override
                    public void onItemClicked(Ride selected_ride, User target_user) {
                        Intent intent = new Intent(getActivity(), Save_info.class);

                        Bundle args = new Bundle();
                        ArrayList<String> selected_ride_data = new ArrayList<>();
                        selected_ride_data.add(selected_ride.getRemoteId() + "");
                        selected_ride_data.add(selected_ride.getUserId() + "");
                        selected_ride_data.add(selected_ride.getFromCity());
                        selected_ride_data.add(selected_ride.getToCity());
                        selected_ride_data.add(selected_ride.getFromState());
                        selected_ride_data.add(selected_ride.getToState());
                        selected_ride_data.add(selected_ride.getFromCountry());
                        selected_ride_data.add(selected_ride.getToCountry());
                        selected_ride_data.add(selected_ride.getDateTime() + "");
                        selected_ride_data.add(selected_ride.getCost() + "");
////////////////////////////////////////
                        ArrayList<String> target_user_data = new ArrayList<>();
                        target_user_data.add(target_user.getRemoteId() + "");
                        target_user_data.add(target_user.getUsername());
                        target_user_data.add(target_user.getPhone());
                        target_user_data.add(target_user.getEmail());
                        args.putStringArrayList("RIDE", selected_ride_data);
                        args.putStringArrayList("USER", target_user_data);
                        intent.putExtra("ARGS", args);
                        startActivity(intent);

                    }
                });
             rv.setLayoutManager(llm);
                rv.setAdapter(adapter);

    }
      });        return v;
    }




    private void findAllRide(final String s, long user_id) {
        MainUserFunctions.find_all_ride(new OnSearchListener() {

            @Override
            public void onSearchSucceed(ArrayList<sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride> matchedRides, Map<Ride, User> matchedRidesData) {
         Toast.makeText(getActivity(), matchedRides.size() + ", " + matchedRidesData.size(), Toast.LENGTH_LONG).show();
                for (Ride ride : matchedRides) {
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
//Toast.makeText(getActivity(), matchedRidesData.get(matchedRides.get(0)).getUsername()+"" + matchedRidesData.size(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSearchFailed(String error) {
     mProgressBar.setVisibility(View.GONE);
            }
        }, getActivity(), s, user_id);


    }
}
