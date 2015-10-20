package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.ServiceHandler;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInboxFetchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.webservice.RequestQueueHandler;

public class TermsOfUse extends Fragment {

    // URL to get contacts JSON
    private static String url ="http://orchidatech.com/sharearide/web-services/term_of_use.php";

    JSONArray termsJSONArray = null;

    private ProgressDialog pDialog;

    private View myFragmentView;
    Typeface font;
    TextView termsView,about_address;
    private  ImageView logo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_terms_of_use, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        termsView = (TextView) myFragmentView.findViewById(R.id.terms);
        about_address = (TextView) myFragmentView.findViewById(R.id.about_address);
        logo=(ImageView)myFragmentView.findViewById(R.id.logo);
        final LinearLayout LL_term_use=(LinearLayout)myFragmentView.findViewById(R.id.LL_term_use);

        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
        termsView.setTypeface(font);
        about_address.setTypeface(font);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();

        logo.getLayoutParams().height=(int)(height*0.31);
        logo.getLayoutParams().width =(int)(height*0.26); // Calling async task to get json
        InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {


                    JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray term_of_useArray = jsonObject.getJSONArray("term_of_use");
                                JSONObject term_of_use = term_of_useArray.getJSONObject(0);
                                String description = term_of_use.getString("description");
                                String name = term_of_use.getString("name");
                                //  Toast.makeText(getActivity(),""+name+ " "+description+"",Toast.LENGTH_LONG).show();
                                termsView.setText(description);
                                LL_term_use.setVisibility(View.VISIBLE);

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
                            LL_term_use.setVisibility(View.VISIBLE);


                        }
                    });
                    RequestQueueHandler.getInstance(getActivity()).addToRequestQueue(request);
                    ///  new GetAbout().execute();

                } else {
                    LL_term_use.setVisibility(View.GONE);

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
