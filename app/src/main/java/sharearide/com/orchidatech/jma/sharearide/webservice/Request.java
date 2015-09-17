package sharearide.com.orchidatech.jma.sharearide.webservice;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Bahaa on 8/9/2015.
 */
public class Request extends JsonObjectRequest {
    private Map<String, String> params;

    public Request(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> params) {
        super(method, url, null, listener, errorListener);
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
