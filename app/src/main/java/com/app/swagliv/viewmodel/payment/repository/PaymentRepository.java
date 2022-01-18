package com.app.swagliv.viewmodel.payment.repository;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.Payment.api.PaymentService;
import com.app.swagliv.model.Payment.pojo.OrderVerifyBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.OrderResponse;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.JsonObject;
import com.razorpay.PaymentData;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    public void getOrderEntity(Subscription subscription, User registeredUser, APIResponseListener apiResponseListener, Integer requestID) {
        PaymentService paymentService = ApplicationRetrofitServices.getInstance().getPaymentService();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", (subscription.getPricePerMonth() * subscription.getDurationInMonths() * 100));
        jsonObject.addProperty("subscriptionId", subscription.getId());
        jsonObject.addProperty("subscriptionName", subscription.getSubscriptionName());
        jsonObject.addProperty("currency", "INR");
        jsonObject.addProperty("durationInMonths", subscription.getDurationInMonths());
        String receiptId = new SimpleDateFormat(AppCommonConstants.DATE_FORMAT).format(new Date()) + "_" + registeredUser.getId();
        // jsonObject.addProperty("receipt_id", booking.getFairCalculation().getInvoiceNo());
        jsonObject.addProperty("receipt_id", receiptId);

        Utility.printLogs("getOrderEntity", jsonObject.toString());

        Call<OrderResponse> call = paymentService.toCreateOrder(jsonObject);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                OrderResponse orderCreatedBaseModel = response.body();
                if (response.isSuccessful() && orderCreatedBaseModel != null) {
                    if (orderCreatedBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(orderCreatedBaseModel, requestID);
                    } else {
                    }

                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);
            }
        });

    }

    public void verifyOrder(Subscription subscription, PaymentData paymentData, APIResponseListener apiResponseListener, Integer requestID) {
        PaymentService paymentService = ApplicationRetrofitServices.getInstance().getPaymentService();
        if (paymentData != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("orderId", paymentData.getOrderId());
            jsonObject.addProperty("paymentId", paymentData.getPaymentId());
            jsonObject.addProperty("razorpaySignature", paymentData.getSignature());
            Call<OrderVerifyBaseModel> call = paymentService.toVerifyOrder(jsonObject);
            call.enqueue(new Callback<OrderVerifyBaseModel>() {
                @Override
                public void onResponse(Call<OrderVerifyBaseModel> call, Response<OrderVerifyBaseModel> response) {
                    OrderVerifyBaseModel orderVerifyBaseModel = response.body();
                    if (response.isSuccessful() && orderVerifyBaseModel != null) {
                        if (orderVerifyBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                            apiResponseListener.onSuccess(orderVerifyBaseModel, requestID);
                        } else {
                            /*apiResponseListener.onSuccess(response.message(), requestID);*/
                        }

                    } else {
                        apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                    }
                }

                @Override
                public void onFailure(Call<OrderVerifyBaseModel> call, Throwable t) {
                    apiResponseListener.onFailure(t, requestID);
                }
            });
        }
    }
}
