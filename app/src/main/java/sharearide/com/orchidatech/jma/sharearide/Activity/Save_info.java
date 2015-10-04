package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

public class Save_info extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Ride ride;
    User user;
    private Button quick_msg,more_info;
    private TextView email, phone, username, cityFrom, cityTo, countryFrom, countryTo, date, time, price,stateFrom,stateTo;
    private ImageButton send_msg, send_mail, call;
    private ArrayList<String> user_data;
private Toolbar tool_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_info);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getHash();

        cityFrom = (TextView) findViewById(R.id.cityFrom);
        cityTo = (TextView) findViewById(R.id.cityTo);
        stateTo = (TextView) findViewById(R.id.stateTo);
        stateFrom = (TextView) findViewById(R.id.stateFrom);
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


        quick_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverEmail = email.getText().toString();
                String senderEmail = UserDAO.getUserById(getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1)).getEmail();
                Toast.makeText(Save_info.this, "", Toast.LENGTH_SHORT).show();

                long profileId = getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1);

                Intent intent = new Intent(Save_info.this, ChatActivity.class);
                intent.putExtra("ReceiverEmail", receiverEmail);
                intent.putExtra("SenderEmail", senderEmail);
                intent.putExtra("ReceiverId", user.getRemoteId());

                intent.putExtra(Common.PROFILE_ID, String.valueOf(profileId));
                startActivity(intent);
            }
        });

        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Save_info.this,MapViewActivity.class));
            }
        });
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("ARGS");
        if (args != null) {
            ArrayList<String> ride_data = args.getStringArrayList("RIDE");
                user_data= args.getStringArrayList("USER");
            ride = new Ride(Long.parseLong(ride_data.get(0)), Long.parseLong(ride_data.get(1)), ride_data.get(2), ride_data.get(3), ride_data.get(4), ride_data.get(5), ride_data.get(6), ride_data.get(7), Long.parseLong(ride_data.get(8)), Double.parseDouble(ride_data.get(9)));
            user = new User(Long.parseLong(user_data.get(0)), null, user_data.get(1), null, null, user_data.get(2), user_data.get(3), null, -1, null);
            email.setText(user.getEmail());
            username.setText(user.getUsername());
            cityFrom.setText(ride.getFromCity());
            cityTo.setText(ride.getToCity());
            stateFrom.setText(ride.getFromState());
            stateTo.setText(ride.getToState());
            price.setText(ride.getCost() + "$");
            countryFrom.setText(ride.getFromCountry());
            countryTo.setText(ride.getToCountry());
            phone.setText(user.getPhone());
            Long date_time = ride.getDateTime();
            String fullDate = AppConstant.DateConvertion.getDate(date_time);
            time.setText(fullDate.split(" ")[1]);
            date.setText(fullDate.split(" ")[0]);

        }

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);

        getLoaderManager().initLoader(0, null, this);

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
}