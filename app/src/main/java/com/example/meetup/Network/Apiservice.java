package com.example.meetup.Network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface Apiservice {
    @POST("send")
    Call<String > sendRemoteMessage(
            @HeaderMap HashMap<String,String>headers,
            @Body String remoteBody
            );
}
