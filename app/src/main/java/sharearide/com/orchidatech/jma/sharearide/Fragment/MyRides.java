package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.MyRidesAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnFetchMyRides;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;

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
    long last_id_server = -1;
    private LinearLayout load_more;



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
                DeleteEditRide addEditRide = new DeleteEditRide();
                addEditRide.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_place, addEditRide).addToBackStack(null).commit();
                getFragmentManager().executePendingTransactions();
//                Toast.makeText(getActivity(), selected_ride.getRemoteId()+"", Toast.LENGTH_LONG).show();
            }
        });
        my_rides_rv.setAdapter(adapter);

        load_more = (LinearLayout) view.findViewById(R.id.btn_load_more);

        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_rides_progress.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                getMyRides();
            }
        });
        InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status)
                    getMyRides();
                else
                {
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
        return view;
    }

    private void getMyRides() {
        MainUserFunctions.get_my_rides(getActivity(), new OnFetchMyRides() {
            @Override
            public void onFetched(ArrayList<Ride> all_my_rides, int count, long last_id) {
                my_rides_progress.setVisibility(View.GONE);
                    my_rides.addAll(all_my_rides);
                    adapter.notifyDataSetChanged();
                load_more.setVisibility(count == -1?View.GONE:View.VISIBLE);
                last_id_server =last_id_server == -1?last_id:last_id_server;


            }

            @Override
            public void onFailed(String error) {
                my_rides_progress.setVisibility(View.GONE);
                load_more.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                //   Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        getFragmentManager().popBackStack();
                ShareRideFragment shareRideFragment = new ShareRideFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRideFragment).commit();
                getFragmentManager().executePendingTransactions();
//                shareRideFragment.selectTab(0);
                    }
                }, 2000);

            }
        }, user.getRemoteId(), llm.getItemCount(), last_id_server);
    }
}
