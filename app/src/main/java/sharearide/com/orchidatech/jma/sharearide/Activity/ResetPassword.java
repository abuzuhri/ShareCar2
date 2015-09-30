package sharearide.com.orchidatech.jma.sharearide.Activity;

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
import sharearide.com.orchidatech.jma.sharearide.R;


public class ResetPassword extends ActionBarActivity {
private Button CancleBtn,SendBtn;
    private Toolbar tool_bar;
    private EditText ed_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        ed_email=(EditText)findViewById(R.id.ed_email);

        CancleBtn=(Button)findViewById(R.id.CancleBtn);
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPassword.this,Login.class);
                startActivity(i);
            }
        });

        SendBtn=(Button)findViewById(R.id.SendBtn);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
