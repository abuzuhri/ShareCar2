package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import com.orchida.android.sharearide.Model.SocialUser;

public interface OnLoginListener {

    void onSuccess(SocialUser user);

    void onFail();
}