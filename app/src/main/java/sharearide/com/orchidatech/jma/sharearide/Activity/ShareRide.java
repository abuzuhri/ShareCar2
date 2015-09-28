package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Fragment.AddRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.DatePicker;
import sharearide.com.orchidatech.jma.sharearide.Fragment.FindRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.TimePicker;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ShareRide extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    //   SlidingTabLayout tabs;
    PagerSlidingTabStrip tabs;
    private ArrayList<Fragment> mFragments;
    AddRide tab1 = new AddRide();
    FindRide tab2 = new FindRide();
    CharSequence Titles[] = {"Offer a Ride", "Find a Ride"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_ride);

        mFragments = new ArrayList<>();
        // Creating The Toolbar and setting it as the Toolbar for the activity
        mFragments.add(tab1);
        mFragments.add(tab2);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, mFragments);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        //   tabs = ( SlidingTabLayout) findViewById(R.id.tabs);
        //  tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setIndicatorColor(getResources().getColor(R.color.tabsScrollColor));
        tabs.setShouldExpand(true);
        tabs.setTextColor(getResources().getColor(R.color.whiteText));
        // tabs.setIndicatorHeight((int)getResources().getDimension(R.dimen.tab_height));
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


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