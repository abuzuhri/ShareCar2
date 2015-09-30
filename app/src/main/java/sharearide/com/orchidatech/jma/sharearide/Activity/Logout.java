
package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnChattingListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;


public class Logout extends AppCompatActivity {
    private ImageView logo;
    private Button inbox,logout_Btn,share_ride_Btn;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);
        logo=(ImageView)findViewById(R.id.logo);
        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().height=(int)(height*0.35);
        logo.getLayoutParams().width
                =(int)(width*0.45);
        logout_Btn=(Button)findViewById(R.id.logout_Btn);
        share_ride_Btn=(Button)findViewById(R.id.share_ride_Btn);
        inbox=(Button)findViewById(R.id.inbox);

        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();
                startActivity(new Intent(Logout.this, Login.class));
            }
        });

        share_ride_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Logout.this,ShareRide.class);
                startActivity(i);
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Logout.this,Inbox.class);
                startActivity(i);
            }
        });
    }

    public void last_chatting_users(final Context context, final OnChattingListListener listener, final long id){
        InternetConnectionChecker.isConnectedToInternet(getApplicationContext(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
                    User user = UserDAO.getUserById(id);
                    String username = user.getUsername();
                    String password = user.getPassword();
                    if (username != null && password != null)
                           MainUserFunctions.last_chatting_users(context, listener, id, username, password);
                } else
                    Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
            }
        });
    }
}
