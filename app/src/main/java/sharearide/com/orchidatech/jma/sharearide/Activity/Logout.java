
package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import sharearide.com.orchidatech.jma.sharearide.R;


public class Logout extends AppCompatActivity {
    private ImageView logo;
    private Button inbox,logout_Btn,share_ride_Btn;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);
        logo=(ImageView)findViewById(R.id.logo);
        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        int width=display.getWidth();
        logo.getLayoutParams().height=(int)(height*0.35);
        logo.getLayoutParams().width
                =(int)(width*0.45);
        logout_Btn=(Button)findViewById(R.id.logout_Btn);
        share_ride_Btn=(Button)findViewById(R.id.share_ride_Btn);
        inbox=(Button)findViewById(R.id.inbox);

        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();
                startActivity(new Intent(Logout.this, Login.class));
            }
        });

        share_ride_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Logout.this,ShareRide.class);
                startActivity(i);
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Logout.this,Inbox.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
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
