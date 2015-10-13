package sharearide.com.orchidatech.jma.sharearide.Chat;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sharearide.com.orchidatech.jma.sharearide.Chat.client.GcmUtil;
import sharearide.com.orchidatech.jma.sharearide.Chat.client.ServerUtilities;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * @author appsrox.com
 */
public class ChatActivity extends ActionBarActivity implements MessagesFragment.OnFragmentInteractionListener {

    private EditText msgEdit;
    private Button sendBtn, cancel;
    //private String profileId;
    private String receiverName;
    //private String profileEmail;

    private GcmUtil gcmUtil;

    String receiverEmail, senderEmail;
    long receiverId, senderId;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getHash();

        // Get sender and receiver emails
        Intent intent = getIntent();
        receiverEmail = intent.getStringExtra("ReceiverEmail");
        receiverId = intent.getLongExtra("ReceiverId", -1);
        senderEmail = intent.getStringExtra("SenderEmail");
        senderId = intent.getLongExtra("SenderId", -1);
Toast.makeText(ChatActivity.this,""+receiverEmail+" , "+senderEmail,Toast.LENGTH_LONG).show();
        //profileId = getIntent().getStringExtra(Common.PROFILE_ID);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (Button) findViewById(R.id.send_btn);
        //cancel = (Button) findViewById(R.id.crosscancel);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //   ActionBar actionBar = getSupportActionBar();
//        toolbar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);

        addContact(receiverEmail);

        /*
        Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, String.valueOf(receiverId)), null, null, null, null);
        if (c.moveToFirst()) {
            receiverName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
            receiverEmail = c.getString(c.getColumnIndex(DataProvider.COL_EMAIL));
           // toolbar.setTitle(receiverName);
        }
        */

        toolbar.setTitle("Receiver Name");
        toolbar.setSubtitle("connecting ...");

        registerReceiver(registrationStatusReceiver, new IntentFilter(Common.ACTION_REGISTER));
        gcmUtil = new GcmUtil(getApplicationContext());
    }

    public void addContact(String email) {

        try {
            ContentValues values = new ContentValues(2);
            values.put(DataProvider.COL_NAME, email.substring(0, email.indexOf('@')));
            values.put(DataProvider.COL_EMAIL, email);
            this.getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if (!msgEdit.getText().toString().isEmpty()) {
                    send(msgEdit.getText().toString());
                    msgEdit.setText(null);
                }
                break;
        }
    }

    public void send(final String txt) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {
                    ServerUtilities.send(txt, senderEmail, receiverEmail);

                    // store chat in local DB
                    ContentValues values = new ContentValues(2);
                    values.put(DataProvider.COL_MSG, txt);
                    values.put(DataProvider.COL_TO, receiverEmail);
                    getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

                    // store chat in server DB
                    long sender_id = getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1);
                    saveMessage(txt, sender_id, receiverId, System.currentTimeMillis());

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    private void saveMessage(String message, long sender_id, long receiverId, long date_time) {
        MainUserFunctions.add_message(getApplicationContext(), message.trim(), sender_id, receiverId, date_time);
    }


    @Override
    public String getProfileEmail() {
        return receiverEmail;
    }


    @Override
    protected void onPause() {
        ContentValues values = new ContentValues(1);
        values.put(DataProvider.COL_COUNT, 0);
        getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, String.valueOf(receiverId)), values, null, null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(registrationStatusReceiver);
        gcmUtil.cleanup();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.close:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    //--------------------------------------------------------------------------------

    private BroadcastReceiver registrationStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Common.ACTION_REGISTER.equals(intent.getAction())) {
                switch (intent.getIntExtra(Common.EXTRA_STATUS, 100)) {
                    case Common.STATUS_SUCCESS:
                        toolbar.setSubtitle("online");
                        sendBtn.setEnabled(true);
                        break;

                    case Common.STATUS_FAILED:
                        //
                        toolbar.setSubtitle("offline");
                        break;
                }
            }
        }
    };

}
