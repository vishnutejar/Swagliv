package com.app.swagliv.model.call.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.call.pojo.TwilioVoiceTokenResponseBaseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TwilioVoiceTokenService {
    @POST(AppConstant.API.GET_TWILIO_ACCESS_TOKEN)
    Call<TwilioVoiceTokenResponseBaseModel> getTwilioAccessToken(
            @Body JsonObject jsonObject
    );
}



