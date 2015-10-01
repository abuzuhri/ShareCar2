package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnChattingListListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;


public class Inbox extends AppCompatActivity {

    private FloatingActionButton addMessage;
    private Toolbar tool_bar;
    private ArrayList<Chat> messages;
    Map<Chat, ArrayList<User>> messagesData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
                tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);

        addMessage = (FloatingActionButton) findViewById(R.id.addMessage);
        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inbox.this, MapViewActivity.class);
                startActivity(i);
            }
        });

        last_chatting_users(this, new OnChattingListListener() {
            @Override
            public void onChattingListRefreshed(ArrayList<Chat> allMessages, Map<Chat, ArrayList<User>> allMessagesData) {

            }
        }, getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1));
    }

    public void last_chatting_users(final Context context, final OnChattingListListener listener, final long id){
        if(id == -1)
            return;

        InternetConnectionChecker.isConnectedToInternet(getApplicationContext(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
                    User user = UserDAO.getUserById(id);

                    if (user != null) {
                        String username = user.getUsername();
                        String password = user.getPassword();
                        MainUserFunctions.last_chatting_users(context, listener, id, username, password);
                    }else{
                        Toast.makeText(context, "This User Not Stored In Local DB", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(context, "No Internet Access..", Toast.LENGTH_LONG).show();
            }
        });
    }

}
