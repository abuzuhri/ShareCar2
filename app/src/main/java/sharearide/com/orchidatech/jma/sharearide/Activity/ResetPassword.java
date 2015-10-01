package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnSendPasswordListener;


public class ResetPassword extends ActionBarActivity {
private Button CancleBtn,SendBtn;
    private Toolbar tool_bar;
    private EditText ed_email;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please Wait...");
        ed_email=(EditText)findViewById(R.id.ed_email);

        CancleBtn=(Button)findViewById(R.id.CancleBtn);
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPassword.this,Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        SendBtn=(Button)findViewById(R.id.SendBtn);
        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ed_email.getText().toString().length() > 0){
                mProgressDialog.show();
                   MainUserFunctions.forgetPassword(getApplicationContext(), ed_email.getText().toString(), new OnSendPasswordListener() {
                       @Override
                       public void onSendingSuccess(String message) {
                            if(mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                           Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                       }

                       @Override
                       public void onSendingFails(String message) {
                           if(mProgressDialog.isShowing())
                               mProgressDialog.dismiss();

                           Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                       }
                   });
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

}
