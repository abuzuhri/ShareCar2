package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppLog;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.FacebookLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.GooglePlusLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.R;
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
    private EditText username,ed_password;
    private CallbackManager callbackManager;

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

        Gobtnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    username.setError("Enter Username");
                } else if (ed_password.getText().toString().equals(""))
                    ed_password.setError("Enter Password");
                else {
                    MainUserFunctions.login(Login.this, username.getText().toString(), ed_password.getText().toString());
                  /*  Intent i = new Intent(Login.this, ShareRide.class);
                    startActivity(i);*/

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


fBbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        facebookLoginClicked();
    }
});
gplusbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        googleLoginClicked();
    }
});
        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().height=(int)(height*0.3);
        logo.getLayoutParams().width =(int)(width*0.35);

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

    /**
     * this method called when New User button pressed
     *
     * @param view the clicked button
     */
    public void newUser(View view) {
        startActivity(new Intent(this, NewUser.class));
    }


    public void facebookLoginClicked() {
        loginWithFacebook(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser user) {
                    Toast.makeText(getApplicationContext(), user.getName() + ", " + user.getEmail() + ", " + user.getAvatarURL(), Toast.LENGTH_LONG).show();
                    Log.i("image", user.getName() + ", " + user.getEmail() + ", " + user.getAvatarURL() + ", " + user.getId());
                }

                @Override
                public void onFail() {
                }
            });

    }


    public void loginWithFacebook(final OnLoginListener lsnr) {
        try {
            FacebookSdk.sdkInitialize(this.getApplicationContext());
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            final AccessToken accessToken = loginResult.getAccessToken();
                            final SocialUser fbUser = new SocialUser();
                            GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                    // Toast.makeText(getApplicationContext(), user.optString("email"), Toast.LENGTH_LONG).show();

//                                    AppLog.i(user.toString());
                                    try {

                                        fbUser.email = user.getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    fbUser.name = user.optString("name");
                                    fbUser.id = user.optString("id");
                                    fbUser.avatarURL = "https://graph.facebook.com/" + fbUser.id + "/picture?width=300&height=300";
                                    fbUser.network = SocialUser.NetworkType.FACEBOOK;

                                    Gson gson = new Gson();
                                    String json = gson.toJson(user);
                                    Log.i("User", json);
//                                    mSharedPreferences.edit().putString(AppConstant.SharedPreferenceNames.SocialUser, json).commit();

                                    lsnr.onSuccess(fbUser);
                                }
                            }).executeAsync();

//                            AppLog.i("LoginManager FacebookCallback onSuccess");
                            if (loginResult.getAccessToken() != null) {
//                                AppLog.i("Access Token:: " + loginResult.getAccessToken());
                                //facebookSuccess();

                            }
//                            AppLog.i("onSuccess");
                        }

                        @Override
                        public void onCancel() {
//                            AppLog.i("onCancel");
//                            AppLog.i("LoginManager FacebookCallback onCancel");
                            // App code
                            lsnr.onFail();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            exception.printStackTrace();
//                            AppLog.i("onError");
//                            AppLog.i("LoginManager FacebookCallback onError");
                            lsnr.onFail();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            lsnr.onFail();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void googleLoginClicked() {
        GooglePlusLogin googlePlusLogin = new GooglePlusLogin(this);
        boolean isLoggedIn = googlePlusLogin.isLoggedIn();
        if (!isLoggedIn) {
            googlePlusLogin.Login(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser socialUser) {
                    Toast.makeText(Login.this,socialUser.getName() + ", " + socialUser.getEmail(),Toast.LENGTH_LONG).show();
                    ///  UserDAO.addNewSocialUser(socialUser);
                    //  MainUserFunctions.signUp(getApplicationContext(), socialUser.getName(), null, socialUser.getAvatarURL(), null, null, null, null, socialUser.getEmail());
                }

                @Override
                public void onFail() {

                }
            });
        }
    }


    public void forgetClicked() {
        String email = editText_retreivePassword.getText().toString();
        UserDAO.retreivePassword(email);
    }


}
