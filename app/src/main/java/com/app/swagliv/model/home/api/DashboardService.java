package com.app.swagliv.model.home.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.home.pojo.DashboardBaseModel;
import com.app.swagliv.model.home.pojo.GetLikeBaseModel;
import com.app.swagliv.model.home.pojo.ProfileActionBaseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DashboardService {

    @POST(AppConstant.API.GET_NEW_PROFILES)
    Call<DashboardBaseModel> getNewProfiles(@Body JsonObject jsonObject);

    @POST(AppConstant.API.ACTION_ON_PROFILE)
    Call<ProfileActionBaseModel> doActionOnProfile(
            @Body JsonObject jsonObject
    );

    @GET(AppConstant.API.GET_USER_LIKE)
    Call<GetLikeBaseModel> getUserLikes();


}
