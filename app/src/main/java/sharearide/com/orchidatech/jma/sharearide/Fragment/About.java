package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    private ProgressDialog pDialog;

    private View myFragmentView;
    TextView addressView, phoneView, emailView;

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
        phoneView = (TextView) myFragmentView.findViewById(R.id.about_phone);
        emailView = (TextView) myFragmentView.findViewById(R.id.about_email);
        logo=(ImageView)myFragmentView.findViewById(R.id.logo);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        logo.getLayoutParams().height = (int) (height * 0.3);
        logo.getLayoutParams().width = (int) (width * 0.25);

        // Calling async task to get json
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray aboutArray = jsonObject.getJSONArray("about");
                    JSONObject about = aboutArray.getJSONObject(0);
                    String fb_account = about.getString("facebook_account");
                    String google_account = about.getString("google_account");
                    String address = about.getString("address");
                    addressView.setText(address);
                    emailView.setText(google_account);

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


    private class GetAbout extends AsyncTask<Void, Void, Void> {

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
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        email = c.getString(TAG_EMAIL);
                        address = c.getString(TAG_ADDRESS);
                        String gender = c.getString(TAG_GENDER);

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject(TAG_PHONE);
                        String mobile = phone.getString(TAG_PHONE_MOBILE);
                        String home = phone.getString(TAG_PHONE_HOME);
                        String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding contact to contact list
                        //contactList.add(contact);

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

            addressView.setText(address);
            emailView.setText(email);
        }

    }


}


