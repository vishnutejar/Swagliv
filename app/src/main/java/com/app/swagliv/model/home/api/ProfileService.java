package com.app.swagliv.model.home.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.common.Common;
import com.app.swagliv.model.home.pojo.PassionListBaseModel;
import com.app.swagliv.model.home.pojo.ProfileActionBaseModel;
import com.app.swagliv.model.home.pojo.UploadImageBaseModel;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.profile.pojo.SubscriptionBaseModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ProfileService {
    @GET(AppConstant.API.GET_ALL_PASSION)
    Call<PassionListBaseModel> getPassionList();

    @GET(AppConstant.API.GET_SUBSCRIPTION_PLAN)
    Call<SubscriptionBaseModel> getSubscriptionPlan();

    @Multipart
    @POST(AppConstant.API.UPLOAD_PROFILE_IMAGES)
    Call<JsonObject> updatePhoto(@Part("imageType") RequestBody imageType,
                                           @Part MultipartBody.Part imgFile);

    @PUT(AppConstant.API.UPDATE_PROFILE)
    Call<LoginResponseBaseModel> doUpdateProfile(@Body JsonObject jsonObject);

    @POST(AppConstant.API.REMOVE_IMAGE)
    Call<LoginResponseBaseModel> doRemoveImage(
            @Body JsonObject jsonObject
    );

}
