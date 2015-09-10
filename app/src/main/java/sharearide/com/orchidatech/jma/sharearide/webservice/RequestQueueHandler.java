package sharearide.com.orchidatech.jma.sharearide.webservice;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Bahaa on 8/9/2015.
 */
public class RequestQueueHandler {

    private static RequestQueueHandler instance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private RequestQueueHandler() {
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public static synchronized RequestQueueHandler getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new RequestQueueHandler();
        }
        return instance;
    }

    public void addToRequestQueue(Request req) {
        requestQueue.add(req);
    }
}
