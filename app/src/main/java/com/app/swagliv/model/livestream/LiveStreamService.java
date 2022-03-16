package com.app.swagliv.model.livestream;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.livestream.pojo.AllLiveStreamCommentsResp;
import com.app.swagliv.model.livestream.pojo.ConnectionsListResp;
import com.app.swagliv.model.livestream.pojo.GetListOfActiveViewers;
import com.app.swagliv.model.livestream.pojo.StartLiveStreamResp;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface LiveStreamService {

    @POST(AppConstant.API.PostCommentOnLiveStream)
    Call<Object> postCommentOnLiveStream(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.StartLiveStream)
    Call<StartLiveStreamResp> startLiveStream(
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
    Call<ConnectionsListResp> getConnectionsList();

    @GET
    Call<AllLiveStreamCommentsResp> getAllLiveStreamComments(@Url String url);

    @POST(AppConstant.API.GetListOfActiveViewers)
    Call<GetListOfActiveViewers> getListOfActiveViewers(
            @Body JsonObject jsonObject
    );
}
