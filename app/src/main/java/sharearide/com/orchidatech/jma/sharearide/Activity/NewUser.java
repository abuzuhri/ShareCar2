package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Logic.Validation;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestListener;


/**
 * Created by Shadow on 8/30/2015.
 */
public class NewUser extends AppCompatActivity {
    Validation validation;
    private static final String DEFAULT_IMAGE = "http://orchidatech.com/sharearide/images/default_image.jpg";

    private Toolbar tool_bar;
    private EditText username,password,re_password,email,phone;
    private Button register;
    private ProgressBar signup_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        signup_progress=(ProgressBar)findViewById(R.id.signup_progress);
        register = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        re_password=(EditText)findViewById(R.id.re_password);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("")) {
                    username.setError("Enter Username");
                } else if (!validation.passwordcontainsNumber(password.getText().toString()))
                    password.setError("Enter Password");
                else if (!re_password.getText().toString().equals(password.getText().toString()))
                    re_password.setError(" Password doesn't match ");
                else if (!validation.isValidEmailAddress(email.getText().toString()))
                    email.setError("Enter correct email ");
                else if (!validation.validatePhoneNumber(phone.getText().toString()))
                    phone.setError("Enter correct Phone ");


                else {
                    signup_progress.setVisibility(View.VISIBLE);
                    register.setVisibility(View.GONE);
                    InternetConnectionChecker.isConnectedToInternet(NewUser.this, new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                MainUserFunctions.signUp(getApplicationContext(), username.getText().toString(), password.getText().toString(), DEFAULT_IMAGE, "",phone.getText().toString() , "", "", email.getText().toString(), new OnRequestListener() {
                                    @Override
                                    public void onFinished() {
                                        signup_progress.setVisibility(View.GONE);
                                        register.setVisibility(View.VISIBLE);
                                    }

                                });


                            } else {
                                signup_progress.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);
                                LayoutInflater li = LayoutInflater.from(NewUser.this);
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewUser.this);

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
                }

            }
        });

    }


    /**
     * this method called when add user button pressed
     *
     * @param view the clicked button
     */
    public void addUser(View view) {
        /*
        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        String repassword = editText_repassword.getText().toString();
        String email = editText_email.getText().toString();

        verifySignUp(username, password, repassword, email);
        */
    }

    /**
     * this method for verify and add the user
     *
     * @param username   of user
     * @param password   of user
     * @param repassword of user
     * @param email      of user
     */
    private void verifySignUp(String username, String password, String repassword, String email) {
        /*
        long userId = 0;
        try {
            userId = UserDAO.addNewUser(username, password, repassword, email);
        } catch (EmptyFieldException e) {
            e.displayMessage();
            e.printStackTrace();
        }
        if (userId != 0) {
            Intent intent = new Intent(NewUser.this, FindaRide.class);
            startActivity(intent);
        } else {
            Toast.makeText(NewUser.this, "Failed to create new user !", Toast.LENGTH_SHORT).show();
        }
        */
    }
}
