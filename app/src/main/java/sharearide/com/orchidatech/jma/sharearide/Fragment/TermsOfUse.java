package sharearide.com.orchidatech.jma.sharearide.Fragment;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sharearide.com.orchidatech.jma.sharearide.Logic.ServiceHandler;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.webservice.RequestQueueHandler;

public class TermsOfUse extends Fragment {

    // URL to get contacts JSON
    private static String url ="http://orchidatech.com/sharearide/web-services/term_of_use.php";

    JSONArray termsJSONArray = null;

    private ProgressDialog pDialog;

    private View myFragmentView;
    Typeface font;
    TextView termsView;
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
        logo=(ImageView)myFragmentView.findViewById(R.id.logo);
        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
        termsView.setTypeface(font);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();

        logo.getLayoutParams().height=(int)(height*0.31);
        logo.getLayoutParams().width =(int)(height*0.26); // Calling async task to get json
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

        return myFragmentView;
    }
}
