package com.app.swagliv.model.Payment.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.Payment.pojo.OrderVerifyBaseModel;
import com.app.swagliv.model.profile.pojo.OrderResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentService {
    @POST(AppConstant.API.CREATE_ORDER)
    Call<OrderResponse> toCreateOrder(
            @Body JsonObject jsonObject
    );

    @POST(AppConstant.API.VERIFY_ORDER)
    Call<OrderVerifyBaseModel> toVerifyOrder(
            @Body JsonObject jsonObject
    );
}
