package com.app.swagliv.model.livestream;

import com.app.swagliv.constant.AppConstant;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LiveStreamService {

    @POST(AppConstant.API.PostCommentOnLiveStream)
    Call<Object> postCommentOnLiveStream(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.CreateConnection)
    Call<Object> createConnection(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.ActionOnConnectionRequest)
    Call<Object> actionOnConnectionRequest(
            @Body JsonObject jsonObject
    );

    @GET(AppConstant.API.GetConnectionsList)
    Call<Object> getConnectionsList();
}
