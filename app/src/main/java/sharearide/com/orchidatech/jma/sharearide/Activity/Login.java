package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Logic.FacebookLogin;
import sharearide.com.orchidatech.jma.sharearide.Logic.GooglePlusLogin;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Shadow on 8/30/2015.
 */
public class Login extends AppCompatActivity {

    EditText editText_username, editText_password, editText_retreivePassword;

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // components inflation goes here..
        // editText_username = (EditText) findViewById(R.id.editText_username);
        // editText_password = (EditText) findViewById(R.id.editText_password);

        // editText_retreivePassword = (EditText) findViewById(R.id.editText_retreivePassword);
    }

    /**
     * This method for verify the username and password
     *
     * @param username of user
     * @param password of user
     */
    private void verifyLogin(String username, String password) {

        if (userDAO.signIn(username, password)) {
            startActivity(new Intent(this, FindRide.class));
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

    /**
     * this method for login with facebook account
     *
     * @param view the clicked button
     */
    public void facebookLoginClicked(View view) {
        FacebookLogin facebookLogin = new FacebookLogin(this);
        boolean isLoggedIn = facebookLogin.isLoggedIn();
        if (!isLoggedIn) {
            //facebookLogin.Login();
        }
    }

    /**
     * this method for login with google account
     *
     * @param view the clicked button
     */
    public void googleLoginClicked(View view) {
        GooglePlusLogin googlePlusLogin = new GooglePlusLogin(this);
        boolean isLoggedIn = googlePlusLogin.isLoggedIn();
        if (!isLoggedIn) {
            //googlePlusLogin.Login();
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
