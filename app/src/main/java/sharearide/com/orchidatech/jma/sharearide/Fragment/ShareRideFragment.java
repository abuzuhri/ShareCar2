package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Activity.ViewPagerAdapter;
import sharearide.com.orchidatech.jma.sharearide.R;

public class ShareRideFragment extends Fragment {
    private AppCompatDelegate mDelegate;

    ViewPager pager;
    ViewPagerAdapter adapter;
    //   SlidingTabLayout tabs;
    PagerSlidingTabStrip tabs;
    private ArrayList<Fragment> mFragments;
    AddRide tab1 = new AddRide();
    FindRide tab2 = new FindRide();
    CharSequence Titles[] = {"Offer a Ride", "Find a Ride"};
    int Numboftabs = 2;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.share_aride, container, false);
        mFragments = new ArrayList<>();
        // Creating The Toolbar and setting it as the Toolbar for the activity
        mFragments.add(tab1);

        mFragments.add(tab2);

        //  tabs.setIndicatorColor(getResources().getColor(R.color.tabsScrollColor));
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getFragmentManager(), Titles, Numboftabs, mFragments);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        //   tabs = ( SlidingTabLayout) findViewById(R.id.tabs);
        //  tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setIndicatorColor(getResources().getColor(R.color.tabsScrollColor));
        tabs.setShouldExpand(true);
        tabs.setTextColor(getResources().getColor(R.color.whiteText));
        // tabs.setIndicatorHeight((int)getResources().getDimension(R.dimen.tab_height));
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return v;
    }

    public void selectTab(int i) {
        if(pager.getCurrentItem() != i)
            pager.setCurrentItem(i);
        Log.i("current tab", pager.getCurrentItem()+"");
    }

}





