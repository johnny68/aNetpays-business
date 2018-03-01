package com.anetpays.sid.business.Login.Extras.Google;

/**
 * Created by siddh on 01-03-2018.
 */

public interface GoogleAuthResponse {

    void onGoogleAuthSignIn(GoogleAuthUser user);

    void onGoogleAuthSignInFailed();

    void onGoogleAuthSignOut(boolean isSuccess);
}
