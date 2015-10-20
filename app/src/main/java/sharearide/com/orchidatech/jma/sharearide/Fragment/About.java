package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sharearide.com.orchidatech.jma.sharearide.Logic.ServiceHandler;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.RequestQueueHandler;
import sharearide.com.orchidatech.jma.sharearide.webservice.UserOperationsProcessor;


public class About extends Fragment {

    // URL to get contacts JSON
    private static String url = "http://orchidatech.com/sharearide/web-services/get_about_data.php";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    Typeface font;
    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    private ProgressDialog pDialog;

    private View myFragmentView;
    TextView addressView, about_facebook,about_gmail,about_twitter,countryView,cityView,comma,about_phone,about_fax;

    private ImageView logo;

    String address, phone, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_about, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        addressView = (TextView) myFragmentView.findViewById(R.id.about_address);
        about_phone = (TextView) myFragmentView.findViewById(R.id.about_phone);
        about_fax = (TextView) myFragmentView.findViewById(R.id.about_fax);
        about_facebook = (TextView) myFragmentView.findViewById(R.id.about_facebook);
        about_twitter = (TextView) myFragmentView.findViewById(R.id.about_twitter);
        about_gmail = (TextView) myFragmentView.findViewById(R.id.about_gmail);
        cityView = (TextView) myFragmentView.findViewById(R.id.city);
        countryView = (TextView) myFragmentView.findViewById(R.id.country);
        comma = (TextView) myFragmentView.findViewById(R.id.comma);
        logo=(ImageView)myFragmentView.findViewById(R.id.logo);
        final LinearLayout ll_about=(LinearLayout)myFragmentView.findViewById(R.id.ll_about);

        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");

        addressView.setTypeface(font);
        cityView.setTypeface(font);
        countryView.setTypeface(font);
        about_fax.setTypeface(font);
        about_phone.setTypeface(font);
        about_facebook.setTypeface(font);
        about_twitter.setTypeface(font);
        about_gmail.setTypeface(font);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();

        logo.getLayoutParams().height=(int)(height*0.31);
        logo.getLayoutParams().width =(int)(height*0.26);
        InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
// Calling async task to get json
                    JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray aboutArray = jsonObject.getJSONArray("about");
                                JSONObject about = aboutArray.getJSONObject(0);
                                String city = about.getString("city");
                                String country = about.getString("country");
                                String phone = about.getString("tel");
                                String fax = about.getString("fax");
                                String fb_account = about.getString("facebook_account");
                                String twitter_account = about.getString("twitter_account");
                                String google_account = about.getString("google_account");
                                String address = about.getString("address");
                                addressView.setText(address);
                                cityView.setText(city);
                                countryView.setText(country);
                                about_fax.setText(fax);
                                about_phone.setText(phone);
                                about_facebook.setText(fb_account);
                                about_twitter.setText(twitter_account);
                                about_gmail.setText(google_account);

                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                            } catch (JSONException e) {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        }
                    });
                    RequestQueueHandler.getInstance(getActivity()).addToRequestQueue(request);
                    ///  new GetAbout().execute();

                } else {
                    ll_about.setVisibility(View.GONE);
                    if (pDialog.isShowing())
                    pDialog.dismiss();
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View v = li.inflate(R.layout.warning, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    // set more_info.xml to alertdialog builder
                    alertDialogBuilder.setView(v);
                    TextView tittle = (TextView) v.findViewById(R.id.tittle);
                    TextView textView7 = (TextView) v.findViewById(R.id.textView7);
                    ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);
                    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
                    tittle.setTypeface(font);
                    textView7.setTypeface(font);


                    // create alert dialog
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    close_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    // show it
                    alertDialog.show();
                }


            }
        });

        return myFragmentView;
    }
}


