package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
                facebookLoginClicked();
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
        FacebookLogin facebookLogin = new FacebookLogin(this);
        boolean isLoggedIn = facebookLogin.isLoggedIn();
        if (!isLoggedIn) {
            facebookLogin.Login(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser socialUser) {
                    Toast.makeText(getApplicationContext(), socialUser.getName() + ", " + socialUser.getEmail() + ", " + socialUser.getAvatarURL(), Toast.LENGTH_LONG).show();
//                    UserDAO.addNewSocialUser(socialUser);
///                    MainUserFunctions.signUp(getApplicationContext(), socialUser.getName(), null, socialUser.getAvatarURL(), null, null, null, null, socialUser.getEmail());

                }

                @Override
                public void onFail() {

                }
            });
        }
    }



        public void googleLoginClicked() {
        GooglePlusLogin googlePlusLogin = new GooglePlusLogin(this);
        boolean isLoggedIn = googlePlusLogin.isLoggedIn();
        if (!isLoggedIn) {
                 googlePlusLogin.Login(new OnLoginListener() {
                @Override
                public void onSuccess(SocialUser socialUser) {
                  ///  UserDAO.addNewSocialUser(socialUser);
                  //  MainUserFunctions.signUp(getApplicationContext(), socialUser.getName(), null, socialUser.getAvatarURL(), null, null, null, null, socialUser.getEmail());
                }

                @Override
                public void onFail() {

                }
            });
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

}
