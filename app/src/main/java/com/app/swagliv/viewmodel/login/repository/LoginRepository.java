package com.app.swagliv.viewmodel.login.repository;


import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.login.api.LoginService;
import com.app.swagliv.model.login.pojo.ContactNumberUpdateBaseModel;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {


    public void doUserRegistration(User user, APIResponseListener apiResponseListener, Integer requestID) {
        LoginService loginServices = ApplicationRetrofitServices.getInstance().getLoginService();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("gender", user.getGender());
        jsonObject.addProperty("dob", user.getDob());
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("password", user.getPassword());
        jsonObject.addProperty("type", AppCommonConstants.LOGIN_TYPE.NORMAL);

        Utility.printLogs("RequestJSON", jsonObject.toString());

        Call<LoginResponseBaseModel> call = loginServices.doRegistration(jsonObject);
        call.enqueue(new Callback<LoginResponseBaseModel>() {
            @Override
            public void onResponse(Call<LoginResponseBaseModel> call, Response<LoginResponseBaseModel> response) {
                LoginResponseBaseModel loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    if (loginResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE)
                        apiResponseListener.onSuccess(loginResponse, requestID);
                    else
                        apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void doUpdateMobileNumber(String mobileNumber, APIResponseListener apiResponseListener, Integer requestID) {

        LoginService loginServices = ApplicationRetrofitServices.getInstance().getLoginService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobileNumber", mobileNumber);
        Utility.printLogs("checkmob", mobileNumber);
        Call<ContactNumberUpdateBaseModel> call = loginServices.doUpdateMobileNumber(jsonObject);
        call.enqueue(new Callback<ContactNumberUpdateBaseModel>() {
            @Override
            public void onResponse(Call<ContactNumberUpdateBaseModel> call, Response<ContactNumberUpdateBaseModel> response) {
                ContactNumberUpdateBaseModel mobileResponse = response.body();
                if (mobileResponse != null) {
                    if (mobileResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(mobileResponse, requestID);
                    } else {
                        apiResponseListener.onSuccess(mobileResponse.getMessage(), requestID);
                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<ContactNumberUpdateBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);

            }
        });

    }

    public void toVerifyOtp(String mobileOtp, String mobileNo, APIResponseListener apiResponseListener, Integer requestID) {
        LoginService loginServices = ApplicationRetrofitServices.getInstance().getLoginService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobileOtp", mobileOtp);
        jsonObject.addProperty("mobileNumber", mobileNo);

        Utility.printLogs("toVerifyOtp", jsonObject.toString());

        Call<LoginResponseBaseModel> call = loginServices.toVerifyOtp(jsonObject);
        call.enqueue(new Callback<LoginResponseBaseModel>() {
            @Override
            public void onResponse(Call<LoginResponseBaseModel> call, Response<LoginResponseBaseModel> response) {
                LoginResponseBaseModel loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    if (loginResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(loginResponse, requestID);
                    } else {
                        apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }


            @Override
            public void onFailure(Call<LoginResponseBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });
    }

    public void doLoginWithEmail(String email, String password, APIResponseListener apiResponseListener, Integer requestID) {
        LoginService loginServices = ApplicationRetrofitServices.getInstance().getLoginService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("type", AppCommonConstants.LOGIN_TYPE.NORMAL);

        Call<LoginResponseBaseModel> call = loginServices.doLogin(jsonObject);
        call.enqueue(new Callback<LoginResponseBaseModel>() {
            @Override
            public void onResponse(Call<LoginResponseBaseModel> call, Response<LoginResponseBaseModel> response) {
                LoginResponseBaseModel loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    if (loginResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(loginResponse, requestID);
                    } else {
                        apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });
    }
}