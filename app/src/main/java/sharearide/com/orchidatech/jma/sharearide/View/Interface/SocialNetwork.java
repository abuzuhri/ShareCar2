package sharearide.com.orchidatech.jma.sharearide.View.Interface;

import android.content.Intent;


public interface SocialNetwork {

    boolean isLoggedIn();

    void Logout();

    void Login(OnLoginListener lsnr);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}