package com.anetpays.sid.business.Constants;

import com.anetpays.sid.business.Constants.models.ServerRequest;
import com.anetpays.sid.business.Constants.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by siddh on 19-02-2018.
 */

public interface RequestInterface
{
    @POST("anetpays/")
    Call<ServerResponse>operation(@Body ServerRequest request);
}
