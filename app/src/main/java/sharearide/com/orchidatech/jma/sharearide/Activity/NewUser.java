package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Utility.EmptyFieldException;


/**
 * Created by Shadow on 8/30/2015.
 */
public class NewUser extends AppCompatActivity {

    EditText editText_username, editText_password, editText_repassword, editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.new_user);

        // components inflation
        editText_username = (EditText) findViewById(R.id.editText_addUser_username);
        editText_password = (EditText) findViewById(R.id.editText_addUser_password);
        editText_repassword = (EditText) findViewById(R.id.editText_addUser_repassword);
        editText_email = (EditText) findViewById(R.id.editText_addUser_email);
        */
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
        long userId = 0;
        try {
            userId = UserDAO.addNewUser(username, password, repassword, email);
        } catch (EmptyFieldException e) {
            e.displayMessage();
            e.printStackTrace();
        }
        if (userId != 0) {
            Intent intent = new Intent(NewUser.this, FindRide.class);
            startActivity(intent);
        } else {
            Toast.makeText(NewUser.this, "Failed to create new user !", Toast.LENGTH_SHORT).show();
        }
    }
}
