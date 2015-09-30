package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.Activity.MoreInfo;
import sharearide.com.orchidatech.jma.sharearide.Chat.ChatActivity;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.RideDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

public class Save_info  extends Activity {
    Ride ride;
    User user;
    private Button quick_msg;
    private TextView email,phone,username,cityFrom,cityTo,countryFrom,countryTo,date,time,price;
    private ImageButton send_msg,send_mail,call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_info);

        cityFrom=(TextView)findViewById(R.id.cityFrom);
        cityTo=(TextView)findViewById(R.id.cityTo);
        countryFrom=(TextView)findViewById(R.id.countryFrom);
        countryTo=(TextView)findViewById(R.id.countryTo);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        price=(TextView)findViewById(R.id.price);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        username=(TextView)findViewById(R.id.username);
        quick_msg=(Button)findViewById(R.id.quick_msg);
        send_mail=(ImageButton)findViewById(R.id.send_mail);
        send_msg=(ImageButton)findViewById(R.id.send_msg);
        call=(ImageButton)findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:"+phone.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                startActivity(callIntent);
            }
        });

        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email.getText().toString(), null));
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
                    Intent intent = new Intent(Save_info.this, ChatActivity.class);
                intent.putExtra("ReceiverEmail", email.getText().toString());
                intent.putExtra("MyEmail", UserDAO.getUserById(getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1)).getEmail());
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("ARGS");
        if(args != null) {
            ArrayList<String> ride_data = args.getStringArrayList("RIDE");
            ArrayList<String> user_data = args.getStringArrayList("USER");
            ride = new Ride(Long.parseLong(ride_data.get(0)), Long.parseLong(ride_data.get(1)), ride_data.get(2), ride_data.get(3), ride_data.get(4), ride_data.get(5), ride_data.get(6), ride_data.get(7), Long.parseLong(ride_data.get(8)), Double.parseDouble(ride_data.get(9)));
            user = new User(Long.parseLong(user_data.get(0)), null, user_data.get(1), null, null, user_data.get(2), user_data.get(3), null, -1, null);
            email.setText(user.getEmail());
            username.setText(user.getUsername());
            cityFrom.setText(ride.getFromCity());
            cityTo.setText(ride.getToCity());
            price.setText(ride.getCost()+"$");
            countryFrom.setText(ride.getFromCountry());
            countryTo.setText(ride.getToCountry());
            phone.setText(user.getPhone());
            Long  date_time=ride.getDateTime();
            String fullDate = AppConstant.DateConvertion.getDate(date_time);
            time.setText(fullDate.split(" ")[1]);
            date.setText(fullDate.split(" ")[0]);

        }

    }
}