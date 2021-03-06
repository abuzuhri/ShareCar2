package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
//import sharearide.com.orchidatech.jma.sharearide.Logic.GMailSender;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Animation.ViewAnimation;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSendPasswordListener;


public class ResetPassword extends ActionBarActivity {
    private Button CancleBtn,SendBtn;
    private Toolbar tool_bar;
    private EditText ed_email;
    private TextView textView,textView2;
    ProgressDialog mProgressDialog;
    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please Wait...");
        ed_email=(EditText)findViewById(R.id.ed_email);
        textView2=(TextView)findViewById(R.id.textView2);
        textView=(TextView)findViewById(R.id.textView);
        SendBtn=(Button)findViewById(R.id.SendBtn);
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");
        fontbold= Typeface.createFromAsset(getAssets(), "fonts/roboto_bold.ttf");
        ed_email.setTypeface(font);
        textView2.setTypeface(font);
        textView.setTypeface(fontbold);
        SendBtn.setTypeface(fontbold);


        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ed_email.getText())) {
                    ed_email.setError("Enter Email ");
                    ViewAnimation.blink(ResetPassword.this, ed_email);
                }else{

                    InternetConnectionChecker.isConnectedToInternet(ResetPassword.this, new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                mProgressDialog.show();

                                MainUserFunctions.forgetPassword(getApplicationContext(), ed_email.getText().toString(), new OnSendPasswordListener() {
                                    @Override
                                    public void onSendingSuccess(String message) {

                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        //            startActivity(new Intent(ResetPassword.this, Login.class));
                                        //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ResetPassword.this.finish();
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onSendingFails(String message) {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                    }
                                });

                            } else {
                                LayoutInflater li = LayoutInflater.from(ResetPassword.this);
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPassword.this);

                                // set more_info.xml to alertdialog builder
                                alertDialogBuilder.setView(v);
                                TextView tittle = (TextView) v.findViewById(R.id.tittle);
                                ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

                                // create alert dialog
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                close_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                // show it
                                alertDialog.show();
                            }

                        }
                    });

                    //////////////////////////////////////

                }
            }
        });
      /*  SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           if(UserDAO.retreivePassword(ed_email.getText().toString())!= null){

             Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                     "mailto", ed_email.getText().toString(), null));
                      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                 emailIntent.putExtra(UserDAO.retreivePassword(ed_email.getText().toString()), "Body");
             startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }else
                  Toast.makeText(ResetPassword.this,"null",Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void sendPassword(String recipient, String password) {
//        GMailSender sender = new GMailSender("amalkhkonz@gmail.com",
//                "a@123456789"); // type ur mail id and password here and next
//        // line also
//        try {
//            sender.sendMail("Recover Password, Share A Ride","Your Password is: "
//                    + password, "amalkhkonz@gmail.com", recipient);
//            if (mProgressDialog.isShowing())
//                mProgressDialog.dismiss();
//
//            Toast.makeText(getApplicationContext(), "Password Send Successfully to your Email :) ", Toast.LENGTH_LONG).show();
//new Handler().postDelayed(new Runnable() {
//    @Override
//    public void run() {
//            ResetPassword.this.finish();
//    }
//}, 1000);
//        } catch (Exception e) {
//            if (mProgressDialog.isShowing())
//                mProgressDialog.dismiss();
//
//            Toast.makeText(getApplicationContext(), "An Error Occurred, try again", Toast.LENGTH_LONG).show();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reset_password, menu);

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
            //push from top to bottom
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //push from top to bottom
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
