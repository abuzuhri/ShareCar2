package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import sharearide.com.orchidatech.jma.sharearide.R;


public class ServicesScreen extends AppCompatActivity implements View.OnClickListener {

    Button login, findRide, offerRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_services);

        /*
        login = (Button) findViewById(R.id.button_login_main);
        login.setOnClickListener(this);

        findRide = (Button) findViewById(R.id.button_findRide_main);
        findRide.setOnClickListener(this);

        offerRide = (Button) findViewById(R.id.button_offerRide_main);
        offerRide.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View v) {

        /*
        if (v.getId() == R.id.button_login_main) {
            startActivity(new Intent(this, Login.class));

        } else if (v.getId() == R.id.button_findRide_main) {
            startActivity(new Intent(this, FindRide.class));

        } else if (v.getId() == R.id.button_offerRide_main) {
            startActivity(new Intent(this, OfferRide.class));
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
