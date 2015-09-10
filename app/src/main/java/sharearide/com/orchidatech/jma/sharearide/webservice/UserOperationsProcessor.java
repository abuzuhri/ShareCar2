package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

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

    public void sendRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> params) {

        Request mRequest = new Request(method, url, listener, errorListener, params);
        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);
    }
}
