package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.LoginManager;
import com.google.android.gms.plus.Plus;

import java.io.ByteArrayOutputStream;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Fragment.About;
import sharearide.com.orchidatech.jma.sharearide.Fragment.FindAllRide;
import sharearide.com.orchidatech.jma.sharearide.Fragment.Inbox;
import sharearide.com.orchidatech.jma.sharearide.Fragment.MyRides;
import sharearide.com.orchidatech.jma.sharearide.Fragment.ShareRideFragment;

import sharearide.com.orchidatech.jma.sharearide.Fragment.TermsOfUse;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.DrawerAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnPhotoClicked;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestFinished;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ShareRide extends ActionBarActivity {
    private static final int RESULT_LOAD_IMAGE = 1;

    // Declaring Your View and Variables
    private ProgressBar  uploadImageProgress;
    ViewPager pager;
    ViewPagerAdapter adapter;
    //   SlidingTabLayout tabs;
    PagerSlidingTabStrip tabs;

    String TITLES[] = {"Offer a Ride","Find a Ride", "My Rides","Inbox","Search","Setting","My Profile", "About", "Terms of Use","Logout"};
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME ;
    String EMAIL;
    String profileImage;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    ShareRideFragment shareRideFragment;
    Inbox inboxFragment;
    FindAllRide findAllRideFragment;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    DrawerAdapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    Inbox inbox=new Inbox();
    private MyRides myRidesFragment;
    private ImageView profileImageView;
    About about=new About();
    TermsOfUse termsOfUse=new TermsOfUse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_ride);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        // Drawer object Assigned to the view
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        shareRideFragment=new ShareRideFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRideFragment).commit();
        getFragmentManager().executePendingTransactions();

        findAllRideFragment= new FindAllRide();

        NAME= UserDAO.getUserById(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1)).getUsername();
        EMAIL=UserDAO.getUserById(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id",-1)).getEmail();
        profileImage = UserDAO.getUserById(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id",-1)).getImage();

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(this, TITLES,NAME,EMAIL,profileImage, new DrawerAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(int position) {

                switch (position) {
                    case 1:
                        Drawer.closeDrawers();
                        shareRideFragment = new ShareRideFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRideFragment).addToBackStack(null).commit();
                        getFragmentManager().executePendingTransactions();
                        shareRideFragment.selectTab(0);
                        break;
                    case 2:
                        Drawer.closeDrawers();
                        shareRideFragment = new ShareRideFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_place, shareRideFragment).addToBackStack(null).commit();
                        getFragmentManager().executePendingTransactions();
                        shareRideFragment.selectTab(1);
                        break;
                    case 3:
                        Drawer.closeDrawers();
                        myRidesFragment = new MyRides();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_place, myRidesFragment).addToBackStack(null).commit();
                        getFragmentManager().executePendingTransactions();
                        toolbar.setTitle("My Rides");


                        break;
                    case 4:
                        Drawer.closeDrawers();
                        inboxFragment = new Inbox();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_place,inboxFragment).addToBackStack(null).commit();
                        getFragmentManager().executePendingTransactions();
                        toolbar.setTitle("Inbox");
                        break;
                    case 5:
                        Drawer.closeDrawers();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_place, findAllRideFragment).addToBackStack(null)
                                .commit();
                        getFragmentManager().executePendingTransactions();
                        toolbar.setTitle("Search Ride");
                        break;
                    case 6:
                        Drawer.closeDrawers();
                        startActivity(new Intent(ShareRide.this, UserSettings.class));
                        break;
                    case 7:
                        Drawer.closeDrawers();
                        startActivity(new Intent(ShareRide.this,UserProfile.class));
                        break;
                    case 8:
                        Drawer.closeDrawers();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_place, about).addToBackStack(null)
                                .commit();
                        getFragmentManager().executePendingTransactions();
                        toolbar.setTitle("About");
                        break;
                    case 9:
                        Drawer.closeDrawers();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_place, termsOfUse).addToBackStack(null)
                                .commit();
                        getFragmentManager().executePendingTransactions();
                        toolbar.setTitle("Terms Of Use");
                        break;
                    case 10:
                        Drawer.closeDrawers();
                        getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();
                        if(getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).getInt("network", -1) == 2){
                            LoginManager.getInstance().logOut();
                            getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();

                        }
                        else if(getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).getInt("network", -1) == 1){
                            if (Login.mGoogleApiClient.isConnected()) {
                                Plus.AccountApi.clearDefaultAccount(Login.mGoogleApiClient);
                                Login.mGoogleApiClient.disconnect();
                                Login.mGoogleApiClient.connect();
                            }
                            getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();

                        }
                        getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();
                        Intent intent=new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();


                }


            }
        }, new OnPhotoClicked() {
            @Override
            public void onClicked(ImageView image,ProgressBar _uploadImageProgress) {
                profileImageView = image;
                uploadImageProgress = _uploadImageProgress;
                Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {

            Uri selectedImage=data.getData();
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            final Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
            if(bitmap == null){
                Toast.makeText(getApplicationContext(), "please choose valid image", Toast.LENGTH_LONG).show();

                return;
            }
            profileImageView.setImageAlpha(100);
            profileImageView.setEnabled(false);

            uploadImageProgress.setVisibility(View.VISIBLE);
//                profileImageView.setImageBitmap(bitmap);

        /*
         * Convert the image to a string
         * */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, 0);
            Log.i("image", image_str);
            MainUserFunctions.uploadImage(getApplicationContext(), getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1), image_str,new OnRequestFinished() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onFinished(String imageUrl) {
                    uploadImageProgress.setVisibility(View.GONE);
                    profileImageView.setImageAlpha(255);
                    profileImageView.setEnabled(true);
                    UserDAO.updateUserPhoto(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1), imageUrl);
                    // Picasso.with(getApplicationContext()).load(Uri.parse(imageUrl)).into(profileImageView);
                    profileImageView.setImageBitmap(bitmap);



                }

                @Override
                public void onFailed() {
                    uploadImageProgress.setVisibility(View.GONE);
                    profileImageView.setImageAlpha(255);
                    profileImageView.setEnabled(true);
                }
            });

        }


    }

}

