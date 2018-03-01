package com.anetpays.sid.business.Constants.models;

/**
 * Created by siddh on 19-02-2018.
 */

public class ServerRequest {
    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
