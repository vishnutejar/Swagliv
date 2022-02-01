package com.app.swagliv.viewmodel.dashboard;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.viewmodel.dashboard.repository.DashboardRepository;

public class DashboardViewModel extends ViewModel implements APIResponseListener {

    public MutableLiveData<APIResponse> apiResponseMutableLiveData;
    private DashboardRepository mDashboardRepository;


    public DashboardViewModel() {
        apiResponseMutableLiveData = new MutableLiveData<>();
        mDashboardRepository = new DashboardRepository();
    }

    public void doGetNearByPeoples(boolean isCurrentSubscription, boolean isMatchedProfile, Integer requestID) {
        apiResponseMutableLiveData.setValue(APIResponse.loading(requestID));
        mDashboardRepository.doGetNearByPeoples(isCurrentSubscription, isMatchedProfile, this, requestID);
    }

    public void doUpdateActionOnPeople(Integer action, String viewerId, Integer requestID) {
        apiResponseMutableLiveData.setValue(APIResponse.loading(requestID));
        mDashboardRepository.doUpdateActionOnPeople(action, viewerId, this, requestID);
    }

    public void getUserLikes(Integer requestId) {
        apiResponseMutableLiveData.setValue(APIResponse.loading(requestId));
        mDashboardRepository.getUserLikes(this, requestId);
    }

    public void doRemoveMatchProfile(String userID, String MatchUserId, Integer requestID) {
         mDashboardRepository.doRemoveMatchProfile(userID, MatchUserId, this, requestID);
    }


    @Override
    public void onSuccess(Object callResponse, Integer requestID) {
        apiResponseMutableLiveData.setValue(APIResponse.success(callResponse, requestID));
    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {
        apiResponseMutableLiveData.setValue(APIResponse.error(error, requestID));
    }
}
