package com.anetpays.sid.business;

/**
 * Created by siddh on 01-03-2018.
 */

interface RPResultListener
{
    void onPermissionGranted();

    void onPermissionDenied();
}
