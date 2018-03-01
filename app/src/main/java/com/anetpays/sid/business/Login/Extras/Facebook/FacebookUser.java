package com.anetpays.sid.business.Login.Extras.Facebook;

import org.json.JSONObject;

/**
 * Created by siddh on 01-03-2018.
 */

public class FacebookUser {
    public String name;

    public String email;

    public String facebookID;

    public String gender;

    public String about;

    public String bio;

    public String coverPicUrl;

    public String profilePic;

    /**
     * JSON response received. If you want to parse more fields.
     */
    public JSONObject response;

}
