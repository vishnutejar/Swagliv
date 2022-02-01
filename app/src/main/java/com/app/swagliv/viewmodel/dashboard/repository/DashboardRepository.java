package com.app.swagliv.viewmodel.dashboard.repository;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.home.api.DashboardService;
import com.app.swagliv.model.home.pojo.DashboardBaseModel;
import com.app.swagliv.model.home.pojo.GetLikeBaseModel;
import com.app.swagliv.model.home.pojo.ProfileActionBaseModel;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    /**
     * Get dashboard peoples list
     *
     * @param apiResponseListener
     * @param requestID
     */
    public void doGetNearByPeoples(boolean isCurrentSubscription, boolean isMatchedProfile, APIResponseListener apiResponseListener, Integer requestID) {
        DashboardService dashboardService = ApplicationRetrofitServices.getInstance().getDashboardService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isCurrentSubscription", isCurrentSubscription);
        jsonObject.addProperty("isMatchedProfile", isMatchedProfile);

        Call<DashboardBaseModel> call = dashboardService.getNewProfiles(jsonObject);
        call.enqueue(new Callback<DashboardBaseModel>() {
            @Override
            public void onResponse(Call<DashboardBaseModel> call, Response<DashboardBaseModel> response) {
                DashboardBaseModel dashboardBaseModel = response.body();
                if (response.isSuccessful() && dashboardBaseModel != null) {
                    if (dashboardBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(dashboardBaseModel.getDataModel(), requestID);
                    } else {

                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }

            }

            @Override
            public void onFailure(Call<DashboardBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });
    }

    public void doUpdateActionOnPeople(Integer action, String viewerId, APIResponseListener apiResponseListener, Integer requestID) {
        DashboardService dashboardService = ApplicationRetrofitServices.getInstance().getDashboardService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("viewerId", viewerId);
        jsonObject.addProperty("action", action);
        Call<ProfileActionBaseModel> call = dashboardService.doActionOnProfile(jsonObject);
        call.enqueue(new Callback<ProfileActionBaseModel>() {
            @Override
            public void onResponse(Call<ProfileActionBaseModel> call, Response<ProfileActionBaseModel> response) {
                ProfileActionBaseModel profileActionBaseModel = response.body();
                if (response.isSuccessful() && profileActionBaseModel != null) {
                    if (profileActionBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(response, requestID);
                    } else {
//                        apiResponseListener.onSuccess(response., requestID);
                    }

                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<ProfileActionBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);

            }
        });

    }

    public void getUserLikes(APIResponseListener apiResponseListener, Integer requestID) {
        DashboardService dashboardService = ApplicationRetrofitServices.getInstance().getDashboardService();
        Call<GetLikeBaseModel> call = dashboardService.getUserLikes();
        call.enqueue(new Callback<GetLikeBaseModel>() {
            @Override
            public void onResponse(Call<GetLikeBaseModel> call, Response<GetLikeBaseModel> response) {
                GetLikeBaseModel getLikeBaseModel = response.body();
                if (response.isSuccessful() && getLikeBaseModel != null) {
                    if (getLikeBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(getLikeBaseModel.getPeopleLikes(), requestID);
                    } else {

                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }

            }

            @Override
            public void onFailure(Call<GetLikeBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);
            }
        });
    }

    public void doRemoveMatchProfile(String userID, String MatchUserId, APIResponseListener apiResponseListener, Integer requestID) {
        DashboardService dashboardService = ApplicationRetrofitServices.getInstance().getDashboardService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("matchedUserid", MatchUserId);
        jsonObject.addProperty("userId",userID );
        /*Call<GetLikeBaseModel> call = dashboardService.getUserLikes();
        call.enqueue(new Callback<GetLikeBaseModel>() {
            @Override
            public void onResponse(Call<GetLikeBaseModel> call, Response<GetLikeBaseModel> response) {
                GetLikeBaseModel getLikeBaseModel = response.body();
                if (response.isSuccessful() && getLikeBaseModel != null) {
                    if (getLikeBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(getLikeBaseModel.getPeopleLikes(), requestID);
                    } else {

                    }
                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }

            }

            @Override
            public void onFailure(Call<GetLikeBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);
            }
        });*/

    }
}
