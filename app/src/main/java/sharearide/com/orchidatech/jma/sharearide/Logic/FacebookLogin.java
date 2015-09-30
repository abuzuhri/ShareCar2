package sharearide.com.orchidatech.jma.sharearide.Logic;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppLog;
import sharearide.com.orchidatech.jma.sharearide.Model.SocialUser;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnLoginListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.SocialNetwork;

public class FacebookLogin implements SocialNetwork {

    private Activity activity;
    protected SharedPreferences mSharedPreferences;
    public CallbackManager callbackManager;


    public FacebookLogin(Activity activity) {
        this.activity = activity;
        mSharedPreferences = activity.getSharedPreferences(activity.getPackageName(), activity.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            return false;
        } else {
            return true;
        }
    }

    public void Logout() {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        mSharedPreferences.edit().remove(AppConstant.SharedPreferenceNames.SocialUser).commit();
        LoginManager.getInstance().logOut();
    }

    public void Login(final OnLoginListener lsnr) {
        try {
            FacebookLogin.getFacebookHashKey(activity);
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            final AccessToken accessToken = loginResult.getAccessToken();
                            final SocialUser fbUser = new SocialUser();
                            GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {
//                                    Toast.makeText(activity, user.optString("email"), Toast.LENGTH_LONG).show();

//                                    AppLog.i(user.toString());
                                    fbUser.email = user.optString("email");
                                    fbUser.name = user.optString("name");
                                    fbUser.id = user.optString("id");
                                    fbUser.avatarURL = "https://graph.facebook.com/" + fbUser.id + "/picture?width=300&height=300";
                                    fbUser.network = SocialUser.NetworkType.FACEBOOK;

                                    Gson gson = new Gson();
                                    String json = gson.toJson(fbUser);
                                    mSharedPreferences.edit().putString(AppConstant.SharedPreferenceNames.SocialUser, json).commit();

                                    lsnr.onSuccess(fbUser);
                                }
                            }).executeAsync();

                            AppLog.i("LoginManager FacebookCallback onSuccess");
                            if (loginResult.getAccessToken() != null) {
                                AppLog.i("Access Token:: " + loginResult.getAccessToken());
                                //facebookSuccess();

                            }
                            AppLog.i("onSuccess");
                        }

                        @Override
                        public void onCancel() {
                            AppLog.i("onCancel");
                            AppLog.i("LoginManager FacebookCallback onCancel");
                            // App code
                            lsnr.onFail();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            exception.printStackTrace();
                            AppLog.i("onError");
                            AppLog.i("LoginManager FacebookCallback onError");
                            lsnr.onFail();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            lsnr.onFail();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void getFacebookHashKey(Context context) {
        AppLog.i("tg" + "Apppp");
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                AppLog.i("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}