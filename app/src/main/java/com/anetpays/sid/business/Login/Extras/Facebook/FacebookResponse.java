package com.anetpays.sid.business.Login.Extras.Facebook;

/**
 * Created by siddh on 01-03-2018.
 */

public interface FacebookResponse {
    void onFbSignInFail();

    void onFbSignInSuccess();

    void onFbProfileReceived(FacebookUser facebookUser);

    void onFBSignOut();
}