package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Typeface;

import sharearide.com.orchidatech.jma.sharearide.Constant.AppLog;
import sharearide.com.orchidatech.jma.sharearide.Constant.UrlConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.FacebookLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.GooglePlusLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Animation.ViewAnimation;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoadFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoginListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperations;

/**
 * Created by Shadow on 8/30/2015.
 */
public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText editText_username, editText_password, editText_retreivePassword;
    UserDAO userDAO;
    private static final int RC_SIGN_IN = 0;

    // Google client to communicate with Google
    private OnGoogleLogged googlelistener;
    public static GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;

    private ImageView logo;
    private TextView signUpbtn,resetPwbtn;
    private ImageButton fBbtn,gplusbtn,Gobtnx;
    private Toolbar tool_bar;
    private EditText ed_email,ed_password;
    CallbackManager callbackManager;
    Typeface font;

    ProgressDialog mProgressDialog ;

    /////////////////////////////////////////////////////
    private GraphRequestAsyncTask mAsyncRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
//        setSupportActionBar(tool_bar);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_password=(EditText)findViewById(R.id.ed_password);
        mProgressDialog= new ProgressDialog(Login.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please Wait...");
//        mProgressDialog.dismiss();
       // font= Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");

        ed_email.setTypeface(font);
        ed_password.setTypeface(font);


        logo=(ImageView)findViewById(R.id.logo);
        fBbtn=(ImageButton)findViewById(R.id.fBbtn);
        gplusbtn=(ImageButton)findViewById(R.id.gplusbtn);
        Gobtnx=(ImageButton)findViewById(R.id.Gobtnx);
        signUpbtn=(TextView)findViewById(R.id.signUpbtn);
        resetPwbtn=(TextView)findViewById(R.id.resetPwbtn);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();

        fBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimation.bounce(Login.this, fBbtn);

                InternetConnectionChecker.isConnectedToInternet(Login.this, new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            facebookLoginClicked(new OnFbLogged() {
                                @Override
                                public void onSuccess(SocialUser user) {
                                    MainUserFunctions.socialSignUp(getApplicationContext(), user);

                                }
                            });

                        } else {

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

            }
        });
        gplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnimation.bounce(Login.this, gplusbtn);

                InternetConnectionChecker.isConnectedToInternet(Login.this, new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {
                            googleLoginClicked(new OnGoogleLogged() {
                                @Override
                                public void onSuccess(SocialUser user) {
                                    mProgressDialog.show();
                                    MainUserFunctions.socialSignUp(getApplicationContext(), user);
                                }
                            });

                        } else {

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

            }
        });

        Gobtnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnimation.clockwise(Login.this, Gobtnx);

                if (ed_email.getText().toString().equals("")) {
                    ed_email.setError("Enter Email");
                    ViewAnimation.blink(Login.this, ed_email);
                    ViewAnimation.bounce(Login.this, Gobtnx);

                } else if (ed_password.getText().toString().equals("")) {
                    ed_password.setError("Enter Password");
                    ViewAnimation.blink(Login.this, ed_password);
                    ViewAnimation.bounce(Login.this, Gobtnx);
                }else {
                    InternetConnectionChecker.isConnectedToInternet(Login.this,new OnInternetConnectionListener() {
                        @Override
                        public void internetConnectionStatus(boolean status) {
                            if (status) {
                                MainUserFunctions.login(Login.this, ed_email.getText().toString(), ed_password.getText().toString(), mProgressDialog);

                            } else {

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

                }
            }
        });


        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, NewUser.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        resetPwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ResetPassword.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });




        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().height=(int)(height*0.31);
        logo.getLayoutParams().width =(int)(height*0.26);

        fBbtn.getLayoutParams().height=(int)(height*0.09);
        fBbtn.getLayoutParams().width =(int)(height*0.09);

        gplusbtn.getLayoutParams().height=(int)(height*0.09);
        gplusbtn.getLayoutParams().width =(int)(height*0.09);

        Gobtnx.getLayoutParams().height=(int)(height*0.12);
        Gobtnx.getLayoutParams().width =(int)(height*0.12);


        /*
        // components inflation goes here..
         editText_username = (EditText) findViewById(R.id.editText_username);
         editText_password = (EditText) findViewById(R.id.editText_password);
         editText_retreivePassword = (EditText) findViewById(R.id.editText_retreivePassword);
         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }

    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
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

    /**
     * this method called when New User button pressed
     *
     * @param view the clicked button
     */
    public void newUser(View view) {
        startActivity(new Intent(this, NewUser.class));
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
                                    fbUser.email = user.optString("email");
                                    fbUser.name = user.optString("name");
                                    fbUser.social_id= user.optString("id");
                                    fbUser.avatarURL = "https://graph.facebook.com/" + user.optString("id") + "/picture?width=300&height=300";
                                    fbUser.network = SocialUser.NetworkType.FACEBOOK;
                                    listener.onSuccess(fbUser);
                                    //    Toast.makeText(getApplicationContext(), user.optString("name"), Toast.LENGTH_LONG).show();


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
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            exception.printStackTrace();
                            AppLog.i("onError");
                            AppLog.i("LoginManager FacebookCallback onError");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void googleLoginClicked(final OnGoogleLogged listener) {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
            googlelistener = listener;
        }
    }
    // TODO: Test

    /**
     * this method call when retreive button clicked
     *
     * @param view the clicked button
     */
    public void forgetClicked(View view) {
        String email = editText_retreivePassword.getText().toString();
        UserDAO.retreivePassword(email);
    }

    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;

        mProgressDialog.dismiss();
        // Toast.makeText(this, "Connected ...", Toast.LENGTH_LONG).show();
        getProfileInformation();
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String social_id = currentPerson.getId();
                SocialUser googleUser = new SocialUser();
               //////////// googleUser.email = email;
                googleUser.name = personName;
                googleUser.avatarURL = personPhotoUrl;
                googleUser.network = SocialUser.NetworkType.GOOGLEPLUS;
                googleUser.social_id = social_id;
                googlelistener.onSuccess(googleUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // store mConnectionResult
            mConnectionResult = result;

            if (signedInUser) {
                resolveSignInError();
            }
        }
    }

    public interface OnFbLogged{
        public void onSuccess(SocialUser user);
    }
    public interface OnGoogleLogged{
        public void onSuccess(SocialUser user);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                signedInUser = false;

            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                Log.i("sds", resultCode+"");
               mGoogleApiClient.connect();
            }

        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);


    }

}