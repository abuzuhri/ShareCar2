package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Bahaa on 8/9/2015.
 */
public class UserOperationsProcessor {

    private static UserOperationsProcessor instance;
    private Context context;

    private UserOperationsProcessor(Context context) {
        this.context = context;
    }

    public static synchronized UserOperationsProcessor getInstance(Context context) {
        if (instance == null) {
            instance = new UserOperationsProcessor(context);
        }
        return instance;
    }

    public void sendRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        Request mRequest = new Request( url, listener, errorListener, params);
//        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);

        JsonObjectRequest mRequest = new JsonObjectRequest(url, null, listener, errorListener);
        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);
    }
}
