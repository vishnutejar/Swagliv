package com.app.swagliv.model.call.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.call.pojo.TokenResponseBaseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TwilioTokenService {
    @POST(AppConstant.API.GET_TWILIO_ACCESS_TOKEN)
    Call<TokenResponseBaseModel> getTwilioAccessToken(
            @Body JsonObject jsonObject
    );
}



