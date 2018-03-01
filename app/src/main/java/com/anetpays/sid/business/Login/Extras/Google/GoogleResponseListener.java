package com.anetpays.sid.business.Login.Extras.Google;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by siddh on 01-03-2018.
 */

public interface GoogleResponseListener {
    void onGSignInFail();

    void onGSignInSuccess(Person personData);
}