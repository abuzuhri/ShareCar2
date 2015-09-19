package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;

import sharearide.com.orchidatech.jma.sharearide.Activity.MoreInfo;
import sharearide.com.orchidatech.jma.sharearide.R;

public class Save_info extends ActionBarActivity {
 private Button quick_msg,more_info;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    //   SlidingTabLayout tabs;
    PagerSlidingTabStrip tabs;

    CharSequence Titles[]={"Find a Ride","Offer a Ride"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_info);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


        tabs = (PagerSlidingTabStrip ) findViewById(R.id.tabs);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setIndicatorColor(getResources().getColor(R.color.tabsScrollColor));
        tabs.setShouldExpand(true);
        tabs.setTextColor(getResources().getColor(R.color.whiteText));
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        quick_msg=(Button)findViewById(R.id.quick_msg);
        more_info=(Button)findViewById(R.id.more_info);

        quick_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Save_info.this,Warning.class);
                startActivity(i);
            }
        });


        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Save_info.this,MoreInfo.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_info, menu);
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
}
