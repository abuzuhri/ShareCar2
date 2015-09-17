package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bahaa on 8/9/2015.
 */
public interface OnLoadFinished {
    public void onSuccess(JSONObject jsonObject) throws JSONException;
    public void onFail(String error);

}
