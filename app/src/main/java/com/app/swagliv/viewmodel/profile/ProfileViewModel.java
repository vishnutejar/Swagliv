package com.app.swagliv.viewmodel.profile;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.PersonalImages;
import com.app.swagliv.viewmodel.profile.repository.ProfileRepository;

public class ProfileViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> mutableLiveData;
    private ProfileRepository profileRepository;

    public ProfileViewModel() {
        mutableLiveData = new MutableLiveData<>();
        profileRepository = new ProfileRepository();
    }


    public void doGetPassionsList(Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        profileRepository.doGetPassionsList(this, requestID);

    }

    public void getSubscriptionPlan(Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        profileRepository.getSubscriptionPlan(this, requestID);

    }

    public void doUpdateProfile(User user, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        profileRepository.doUpdateProfile(user, this, requestID);

    }

    public void getPurchaseHistory(Context context, String filePath, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        profileRepository.getPurchaseHistory(context, filePath, this, requestID);

    }

    public void removeImage(PersonalImages personalImages, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        profileRepository.removeImage(personalImages, this, requestID);

    }

    @Override
    public void onSuccess(Object callResponse, Integer requestID) {
        mutableLiveData.setValue(APIResponse.success(callResponse, requestID));
    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {
        mutableLiveData.setValue(APIResponse.error(error, requestID));
    }
}
