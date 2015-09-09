package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orchida.android.sharearide.Logic.DriverInfo;
import com.orchida.android.sharearide.R;

/**
 * Created by Shadow on 8/31/2015.
 */
// TODO: check functions ..
public class MoreInfoDriver extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info);
    }

    public void moreInfoClicked(View view) {
        DriverInfo.showInputDialog(this);
    }

    public void callClicked(View view) {
        DriverInfo.CallIntent(this, "12345678");
    }

    public void messageClicked(View view) {
        DriverInfo.MessageIntent(this, "12345678", "body");
    }

    public void emailClicked(View view) {
        DriverInfo.EmailIntent(this, "to", "", "subject", "body");
    }

    public void quickMessageClicked(View view) {
        DriverInfo.QuickMessageIntent(this);
    }

}
