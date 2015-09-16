package findaride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import sharearide.com.orchidatech.jma.sharearide.R;


public class Login extends ActionBarActivity {
private  ImageView logo;
    private Button signUpbtn,resetPwbtn;
    private ImageButton fBbtn,gplusbtn,Gobtnx;
    private Toolbar tool_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);

        logo=(ImageView)findViewById(R.id.logo);
        fBbtn=(ImageButton)findViewById(R.id.fBbtn);
        gplusbtn=(ImageButton)findViewById(R.id.gplusbtn);
        Gobtnx=(ImageButton)findViewById(R.id.Gobtnx);
        signUpbtn=(Button)findViewById(R.id.signUpbtn);
        resetPwbtn=(Button)findViewById(R.id.resetPwbtn);

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Login.this,Signup.class);
                startActivity(i);
            }
        });

        resetPwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Login.this,ResetPassword.class);
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

        Gobtnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this,ShareRide.class);
                startActivity(i);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
