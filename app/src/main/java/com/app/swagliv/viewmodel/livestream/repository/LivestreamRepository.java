package com.app.swagliv.viewmodel.livestream.repository;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.livestream.LiveStreamService;
import com.app.swagliv.model.livestream.pojo.AllLiveStreamCommentsResp;
import com.app.swagliv.model.livestream.pojo.ConnectionsListResp;
import com.app.swagliv.model.livestream.pojo.GetListOfActiveViewers;
import com.app.swagliv.model.livestream.pojo.StartLiveStreamResp;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.firebase.crashlytics.internal.settings.model.AppRequestData;
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

    public void createConnection(String viewerId, APIResponseListener apiResponseListener, Integer requestID) {
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

        Call<ConnectionsListResp> call = liveStreamService.getConnectionsList();
        call.enqueue(new Callback<ConnectionsListResp>() {
            @Override
            public void onResponse(Call<ConnectionsListResp> call, Response<ConnectionsListResp> response) {
                ConnectionsListResp loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<ConnectionsListResp> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void getAllLiveStreamComments(String url, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();

        Call<AllLiveStreamCommentsResp> call = liveStreamService.getAllLiveStreamComments(url);
        call.enqueue(new Callback<AllLiveStreamCommentsResp>() {
            @Override
            public void onResponse(Call<AllLiveStreamCommentsResp> call, Response<AllLiveStreamCommentsResp> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<AllLiveStreamCommentsResp> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void getListOfActiveViewers(String liveStreamId, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("liveStreamId", liveStreamId);
        Call<GetListOfActiveViewers> call = liveStreamService.getListOfActiveViewers(jsonObject);
        call.enqueue(new Callback<GetListOfActiveViewers>() {
            @Override
            public void onResponse(Call<GetListOfActiveViewers> call, Response<GetListOfActiveViewers> response) {
                Object loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<GetListOfActiveViewers> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void startLiveStream(String resourceURI, APIResponseListener apiResponseListener, Integer requestID) {
        LiveStreamService liveStreamService = ApplicationRetrofitServices.getInstance().getLiveStreamService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("resourceURI", resourceURI);
        Utility.printLogs("RequestJSON", jsonObject.toString());
        Call<StartLiveStreamResp> call = liveStreamService.startLiveStream(jsonObject);
        call.enqueue(new Callback<StartLiveStreamResp>() {
            @Override
            public void onResponse(Call<StartLiveStreamResp> call, Response<StartLiveStreamResp> response) {
                StartLiveStreamResp loginResponse = response.body();
                if (response.isSuccessful() && loginResponse != null) {
                    //----------
                    apiResponseListener.onSuccess(loginResponse, requestID);
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<StartLiveStreamResp> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });
    }
}
