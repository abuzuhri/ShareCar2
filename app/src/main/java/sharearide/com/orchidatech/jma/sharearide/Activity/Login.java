package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import sharearide.com.orchidatech.jma.sharearide.Constant.AppLog;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.FacebookLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.GooglePlusLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoginListener;

/**
 * Created by Shadow on 8/30/2015.
 */
public class Login extends AppCompatActivity {

    EditText editText_username, editText_password, editText_retreivePassword;
    UserDAO userDAO;
    private ImageView logo;
    private Button signUpbtn,resetPwbtn;
    private ImageButton fBbtn,gplusbtn,Gobtnx;
    private Toolbar tool_bar;
    CallbackManager callbackManager;

    private EditText username,ed_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        username=(EditText)findViewById(R.id.username);
        ed_password=(EditText)findViewById(R.id.ed_password);

        logo=(ImageView)findViewById(R.id.logo);
        fBbtn=(ImageButton)findViewById(R.id.fBbtn);
        gplusbtn=(ImageButton)findViewById(R.id.gplusbtn);
        Gobtnx=(ImageButton)findViewById(R.id.Gobtnx);
        signUpbtn=(Button)findViewById(R.id.signUpbtn);
        resetPwbtn=(Button)findViewById(R.id.resetPwbtn);

        fBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginClicked(new OnFbLogged() {
                    @Override
                    public void onSuccess(SocialUser user) {
                        MainUserFunctions.socialSignUp(getApplicationContext(), user);

                    }
                });
            }
        });

        gplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLoginClicked();
            }
        });

        Gobtnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    username.setError("Enter Username");
                } else if (ed_password.getText().toString().equals(""))
                    ed_password.setError("Enter Password");
                else {
                    //////////////////////////////////////////
                    InternetConnectionChecker.isConnectedToInternet(Login.this, new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                MainUserFunctions.login(Login.this, username.getText().toString(), ed_password.getText().toString());


                            } else{
                                LayoutInflater li = LayoutInflater.from(Login.this);
                                View v = li.inflate(R.layout.warning, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);

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
                    //////////////////////////////////////////

                }
            }
        });


        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Login.this, NewUser.class);
                startActivity(i);
            }
        });

        resetPwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Login.this, ResetPassword.class);
                startActivity(i);
            }
        });




        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().height=(int)(height*0.3);
        logo.getLayoutParams().width =(int)(width*0.4);

        fBbtn.getLayoutParams().height=(int)(height*0.09);
        fBbtn.getLayoutParams().width =(int)(width*0.15);

        gplusbtn.getLayoutParams().height=(int)(height*0.09);
        gplusbtn.getLayoutParams().width =(int)(width*0.15);

        Gobtnx.getLayoutParams().height=(int)(height*0.12);
        Gobtnx.getLayoutParams().width =(int)(width*0.2);


        /*

        // components inflation goes here..
         editText_username = (EditText) findViewById(R.id.editText_username);
         editText_password = (EditText) findViewById(R.id.editText_password);

         editText_retreivePassword = (EditText) findViewById(R.id.editText_retreivePassword);
         */
    }

    /**
     * This method for verify the username and password
     *
     * @param username of user
     * @param password of user
     */
    private void verifyLogin(String username, String password) {

        if (userDAO.signIn(username, password)) {
            startActivity(new Intent(this, ShareRide.class));
        } else {
            Toast.makeText(this, "Please check your username and password !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this method called when Sign in button pressed
     *
     * @param view the clicked button
     */
    public void signIn(View view) {

        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();

        verifyLogin(username, password);
    }

    public void facebookLoginClicked(final OnFbLogged listener) {
        try {

            FacebookSdk.sdkInitialize(this);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            final AccessToken accessToken = loginResult.getAccessToken();
                            final SocialUser fbUser = new SocialUser();
                            GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {

//                                    AppLog.i(user.toString());

                                    fbUser.email = user.optString("email");
                                    fbUser.name = user.optString("name");
                                    fbUser.id = user.optString("id");
                                    fbUser.avatarURL = "https://graph.facebook.com/" + user.optString("id") + "/picture?width=300&height=300";
                                    fbUser.network = SocialUser.NetworkType.FACEBOOK;
                                    listener.onSuccess(fbUser);
                                    Toast.makeText(getApplicationContext(), user.optString("name"), Toast.LENGTH_LONG).show();


                                }
                            }).executeAsync();

                            AppLog.i("LoginManager FacebookCallback onSuccess");
                            if (loginResult.getAccessToken() != null) {
                                AppLog.i("Access Token:: " + loginResult.getAccessToken());
                                //facebookSuccess();

                            }
                            AppLog.i("onSuccess");
                        }

                        @Override
                        public void onCancel() {
                            AppLog.i("onCancel");
                            AppLog.i("LoginManager FacebookCallback onCancel");
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            exception.printStackTrace();
                            AppLog.i("onError");
                            AppLog.i("LoginManager FacebookCallback onError");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }



    }




    public void googleLoginClicked() {
        GooglePlusLogin googlePlusLogin =  GooglePlusLogin.getInstance(this,null);
        boolean isLoggedIn = googlePlusLogin.isLoggedIn();
        //   if (!isLoggedIn) {
        googlePlusLogin.Login(new OnLoginListener() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                Toast.makeText(Login.this, socialUser.getName() + "", Toast.LENGTH_LONG).show();
                ///  UserDAO.addNewSocialUser(socialUser);
                MainUserFunctions.socialSignUp(getApplicationContext(), socialUser);
            }

            @Override
            public void onFail() {

            }
        });
        //   }
    }
    public interface OnFbLogged{
        public void onSuccess(SocialUser user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT_CODE", requestCode + "");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
