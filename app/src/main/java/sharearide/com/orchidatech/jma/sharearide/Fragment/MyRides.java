package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyRidesAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnFetchMyRides;

/**
 * Created by Bahaa on 7/10/2015.
 */
public class MyRides extends Fragment {
    @Nullable
    ArrayList<Ride> my_rides;
    MyRidesAdapter adapter;
    RecyclerView my_rides_rv;
    ProgressBar my_rides_progress;
    private LinearLayoutManager llm;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_rides_fragment, null, false);
        my_rides_rv = (RecyclerView) view.findViewById(R.id.my_rides_rv);
        my_rides_progress = (ProgressBar) view.findViewById(R.id.my_rides_progress);
        my_rides_rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        my_rides_rv.setLayoutManager(llm);
        my_rides = new ArrayList<>();
        user = UserDAO.getUserById(getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
        adapter = new MyRidesAdapter(getActivity(), my_rides, user, new MyRidesAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(Ride selected_ride) {
               Bundle args =  new Bundle();
                args.putLong("id", selected_ride.getRemoteId());
                AddEditRide addEditRide = new AddEditRide();
                addEditRide.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_place, addEditRide).commit();
                getFragmentManager().executePendingTransactions();
//                Toast.makeText(getActivity(), selected_ride.getRemoteId()+"", Toast.LENGTH_LONG).show();
            }
        });
        my_rides_rv.setAdapter(adapter);
        getMyRides();
        return view;
    }

    private void getMyRides() {
        MainUserFunctions.get_my_rides(getActivity(), new OnFetchMyRides() {
            @Override
            public void onFetched(ArrayList<Ride> all_my_rides) {
                my_rides.addAll(all_my_rides);
                my_rides_progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                my_rides_progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

            }
        }, user.getRemoteId());
    }
}
