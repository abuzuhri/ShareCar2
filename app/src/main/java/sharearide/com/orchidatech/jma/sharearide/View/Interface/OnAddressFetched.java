package sharearide.com.orchidatech.jma.sharearide.View.Interface;


import java.util.Map;

/**
 * Created by Amal on 10/6/2015.
 */
public interface OnAddressFetched {
    public void onFetched(Map<String, String> address);
    public void onFailed(String error);
}
