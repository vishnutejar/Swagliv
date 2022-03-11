package com.app.swagliv.viewmodel.livestream.repository;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.livestream.LiveStreamService;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivestreamRepository {
    public void postCommentOnLiveStream(String liveStreamId, String comment, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("liveStreamId", liveStreamId);
        jsonObject.addProperty("comment", comment);
        Utility.printLogs("RequestJSON", jsonObject.toString());
        Call<Object> call = liveStreamService.postCommentOnLiveStream(jsonObject);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                    //  apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void createConnection(String viewerId, String comment, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("viewerId", viewerId);
        Utility.printLogs("RequestJSON", jsonObject.toString());
        Call<Object> call = liveStreamService.createConnection(jsonObject);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                    //  apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void actionOnConnectionRequest(String connectionId, String connectionStatus, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("connectionId", connectionId);
        jsonObject.addProperty("connectionStatus", connectionStatus);//accepted
        Utility.printLogs("RequestJSON", jsonObject.toString());
        Call<Object> call = liveStreamService.actionOnConnectionRequest(jsonObject);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                    //  apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void getConnectionsList(APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();

        Call<Object> call = liveStreamService.getConnectionsList();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                    //  apiResponseListener.onSuccess(loginResponse.getMessage(), requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

}
