package sharearide.com.orchidatech.jma.sharearide.View.Interface;


import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;

public interface OnLoginListener {

    void onSuccess(SocialUser user);

    void onFail();
}