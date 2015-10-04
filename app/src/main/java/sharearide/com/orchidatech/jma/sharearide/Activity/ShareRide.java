package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Fragment.AddRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.FindAllRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.FindRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.Inbox;
import sharearide.com.orchidatech.jma.sharearide.Fragment.SearchRideFragment;
import sharearide.com.orchidatech.jma.sharearide.Fragment.ShareRideFragment;

import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.DrawerAdapter;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ShareRide extends ActionBarActivity {

    // Declaring Your View and Variables

    ViewPager pager;
    ViewPagerAdapter adapter;
    //   SlidingTabLayout tabs;
    PagerSlidingTabStrip tabs;
    private ArrayList<Fragment> mFragments;
    AddRide tab1 = new AddRide();
    FindRide tab2 = new FindRide();
    CharSequence Titles[] = {"Offer a Ride", "Find a Ride"};
    int Numboftabs = 2;
    String TITLES[] = {"Offer a Ride","Find a Ride","Inbox","Search","Setting","Logout"};
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME ;
    String EMAIL;
    int PROFILE = R.drawable.emp;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    DrawerAdapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    ShareRideFragment shareRid;
    Inbox inbox=new Inbox();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_ride);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
              // Drawer object Assigned to the view
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        shareRid=new ShareRideFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRid).commit();
        getFragmentManager().executePendingTransactions();

        NAME= UserDAO.getUserById(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1)).getUsername();
        EMAIL=UserDAO.getUserById(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id",-1)).getEmail();

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(TITLES,NAME,EMAIL,PROFILE, new DrawerAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(int position) {
                switch (position) {
                    case 1:
                        Drawer.closeDrawers();
                        if(!shareRid.isAdded()) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRid).addToBackStack(null).commit();
                            getFragmentManager().executePendingTransactions();

                        }

                        shareRid.selectTab(0);
//                            ((ShareRideFragment) getFragmentManager().findFragmentById(R.id.fragment_place)).selectTab(0);
//                        if(!shareRide.isInLayout())
//                            shareRide.selectTab(1);
                        break;
                    case 2:
                        Drawer.closeDrawers();

                        if(!shareRid.isAdded()) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRid).addToBackStack(null).commit();
                            getFragmentManager().executePendingTransactions();
                        }

                                shareRid.selectTab(1);

//                        ((ShareRideFragment) getFragmentManager().findFragmentById(R.id.fragment_place)).selectTab(1);

                    break;
                    case 3:
                        Drawer.closeDrawers();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_place,inbox).addToBackStack(null).commit();
                        getFragmentManager().executePendingTransactions();
                        break;
                    case 4:
                        Drawer.closeDrawers();


                        Fragment mainFragment = new FindAllRide();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(android.R.id.content, mainFragment).addToBackStack(null)
                                .commit();
                        getFragmentManager().executePendingTransactions();
                        break;
                    case 5:
                        Drawer.closeDrawers();

                        Intent iaa = new Intent(ShareRide.this, Main.class);
                        startActivity(iaa);
                        break;
                    case 6:
                        Drawer.closeDrawers();
                        getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();
                        Intent intent=new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();

                    default:

                }


            }
        });       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);
        // Setting the layout Manager
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public void onDateSet(int year, int month, int day) {

        if (pager.getCurrentItem() == 0)
            tab1.onDateSet(year, month, day);
        else
            tab2.onDateSet(year, month, day);
    }*/
/*
    @Override
    public void onTimeSet(int hour, int minute) {
        if(pager.getCurrentItem() == 0)
            tab1.onTimeSet(hour, minute);
        else
            tab2.onTimeSet(hour, minute);
    }*/
}