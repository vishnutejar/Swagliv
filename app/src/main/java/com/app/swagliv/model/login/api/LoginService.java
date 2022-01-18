package com.app.swagliv.model.login.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.login.pojo.ContactNumberUpdateBaseModel;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST(AppConstant.API.REGISTER)
    Call<LoginResponseBaseModel> doRegistration(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.UPDATE_MOBILE_NUMBER)
    Call<ContactNumberUpdateBaseModel> doUpdateMobileNumber(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.VERIFY_MOBILE_OTP)
    Call<LoginResponseBaseModel> toVerifyOtp(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.LOGIN)
    Call<LoginResponseBaseModel> doLogin(
            @Body JsonObject jsonObject
    );
}



