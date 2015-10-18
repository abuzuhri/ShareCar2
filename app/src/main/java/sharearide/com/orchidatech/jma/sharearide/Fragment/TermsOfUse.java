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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sharearide.com.orchidatech.jma.sharearide.Logic.ServiceHandler;
import sharearide.com.orchidatech.jma.sharearide.R;

public class TermsOfUse extends Fragment {

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_TERMS = "terms";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";

    JSONArray termsJSONArray = null;

    private ProgressDialog pDialog;

    private View myFragmentView;
Typeface font;
    TextView termsView;
    String terms;
    private  ImageView logo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_terms_of_use, container, false);

        termsView = (TextView) myFragmentView.findViewById(R.id.terms);
        logo=(ImageView)myFragmentView.findViewById(R.id.logo);
        font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
        termsView.setTypeface(font);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();

        logo.getLayoutParams().height=(int)(height*0.31);
        logo.getLayoutParams().width =(int)(height*0.26); // Calling async task to get json
        new GetTermsOfUse().execute();

        return myFragmentView;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetTermsOfUse extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    termsJSONArray = jsonObj.getJSONArray(TAG_TERMS);

                    // looping through All Contacts
                    for (int i = 0; i < termsJSONArray.length(); i++) {
                        JSONObject c = termsJSONArray.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject(TAG_PHONE);
                        String mobile = phone.getString(TAG_PHONE_MOBILE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //termsView.setText(terms);
        }

    }

}
