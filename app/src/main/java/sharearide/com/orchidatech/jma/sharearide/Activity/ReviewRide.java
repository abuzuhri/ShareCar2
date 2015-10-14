package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Chat.ChatActivity;
import sharearide.com.orchidatech.jma.sharearide.Chat.Common;
import sharearide.com.orchidatech.jma.sharearide.Chat.DataProvider;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

public class ReviewRide extends ActionBarActivity {
    Ride ride;
    User user;
    private Button quick_msg,more_info;
    private TextView email, phone, username, cityFrom, cityTo,stateFrom,stateTo, countryFrom, countryTo, date, time, price;
    private ImageButton send_msg, send_mail, call;
    private ArrayList<String> user_data;
    private  Toolbar tool_bar;
    private EditText info;
    String more_Info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_info);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getHash();

        cityFrom = (TextView) findViewById(R.id.cityFrom);
        cityTo = (TextView) findViewById(R.id.cityTo);
        stateFrom = (TextView) findViewById(R.id.stateFrom);
        stateTo = (TextView) findViewById(R.id.stateTo);
        countryFrom = (TextView) findViewById(R.id.countryFrom);
        countryTo = (TextView) findViewById(R.id.countryTo);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        price = (TextView) findViewById(R.id.price);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        username = (TextView) findViewById(R.id.username);
        quick_msg = (Button) findViewById(R.id.quick_msg);
        more_info = (Button) findViewById(R.id.more_info);
        send_mail = (ImageButton) findViewById(R.id.send_mail);
        send_msg = (ImageButton) findViewById(R.id.send_msg);
        call = (ImageButton) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + phone.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                startActivity(callIntent);
            }
        });

        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email.getText().toString(), null));
                //          emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + phone.getText().toString())));
            }
        });
        // set more_info.xml to alertdialog builder
        LayoutInflater li = LayoutInflater.from(ReviewRide.this);
        View dialogView = li.inflate(R.layout.more_info, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ReviewRide.this);

        alertDialogBuilder.setView(dialogView);
        TextView tittle = (TextView) dialogView.findViewById(R.id.tittle);
        final ImageButton confirm_btn = (ImageButton) dialogView.findViewById(R.id.confirm_btn);
        info = (EditText) dialogView.findViewById(R.id.info);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        more_Info = info.getText().toString();
                        alertDialog.dismiss();
                    }
                });
                info.setText(more_Info);

                // show it
                alertDialog.show();



//                Toast.makeText(ReviewRide.this,""+ride.getFrom_Lattitude()+" , "+ride.getFrom_Longitude(),Toast.LENGTH_LONG).show();
//                Toast.makeText(ReviewRide.this,""+ride.getTo_latitude()+" , "+ride.getTo_longitude(),Toast.LENGTH_LONG).show();


            }
        });

        quick_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String receiverEmail = email.getText().toString();
                long receiverId = user.getRemoteId();
                String senderEmail = UserDAO.getUserById(getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1)).getEmail();
                long senderId = getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1);

//                Toast.makeText(ReviewRide.this, "Sender: " + senderEmail + " " + senderId +
//                        "\nReceiver: " + receiverEmail + " " + receiverId, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ReviewRide.this, ChatActivity.class);
                intent.putExtra("ReceiverEmail", receiverEmail);
                intent.putExtra("ReceiverId", user.getRemoteId());
                intent.putExtra("SenderEmail", senderEmail);
                intent.putExtra("SenderId", senderId);
                //intent.putExtra(Common.PROFILE_ID, String.valueOf(myId));
                startActivity(intent);
            }
        });


        Intent intent = getIntent();

        long ride_id = intent.getLongExtra("ride_id", -1);
        long user_id = intent.getLongExtra("user_id", -1);
        ride = RideDAO.getRideByRemoteId(ride_id);
        user = UserDAO.getUserById(user_id);
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        cityFrom.setText(ride.getFromCity());
        cityTo.setText(ride.getToCity());
        stateFrom.setText(ride.getFromState());
        stateTo.setText(ride.getToState());
        price.setText(ride.getCost());
        countryFrom.setText(ride.getFromCountry());
        countryTo.setText(ride.getToCountry());
        phone.setText(user.getPhone());
        Long date_time = ride.getDateTime();
        String fullDate = AppConstant.DateConvertion.getDate(date_time);
        time.setText(fullDate);
        more_Info=ride.getMore_info();
        //    Toast.makeText(ReviewRide.this,""+ride.getMore_info(),Toast.LENGTH_LONG).show();

//        Toast.makeText(ReviewRide.this,""+ride.getTo_latitude()+" , "+ride.getTo_longitude(),Toast.LENGTH_LONG).show();

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        //getLoaderManager().initLoader(0, null, this);
    }

    private void getHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.i("key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
    /*
    // ----------------------------------------------------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_PROFILE, new String[]{
                DataProvider.COL_ID, DataProvider.COL_NAME,
                DataProvider.COL_COUNT}, null, null,
                DataProvider.COL_ID + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //adapter.swapCursor(null);
    }
    */


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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent =new Intent(ReviewRide.this,MapViewActivity.class);
            Bundle b=new Bundle();
            b.putDouble("FromCityLattitude", ride.getFrom_Lattitude());
            b.putDouble("FromCityLongitude", ride.getFrom_Longitude() );
            b.putDouble("ToCityLattitude", ride.getTo_latitude() );
            b.putDouble("ToCityLongitude", ride.getTo_longitude() );
            //  Toast.makeText(ReviewRide.this,ride.getFrom_Lattitude()+"",Toast.LENGTH_LONG).show();
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}