package com.app.swagliv.viewmodel.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.login.repository.LoginRepository;

public class LoginViewModel extends ViewModel implements APIResponseListener {

    public MutableLiveData<APIResponse> responseMutableLiveData;
    private LoginRepository loginRepository;

    public LoginViewModel() {
        responseMutableLiveData = new MutableLiveData<>();
        loginRepository = new LoginRepository();
    }

    public void doUserRegistration(User user, int type, Integer requestID, String deviceId) {
        responseMutableLiveData.setValue(APIResponse.loading(requestID));
        loginRepository.doUserRegistration(user, type, this, requestID, deviceId);

    }

    public void doUpdateMobileNumber(String mobileNumber, Integer requestID) {
        responseMutableLiveData.setValue(APIResponse.loading(requestID));
        loginRepository.doUpdateMobileNumber(mobileNumber, this, requestID);
    }

    public void toVerifyOtp(String otp, String mobileNo, Integer requestID) {
        responseMutableLiveData.setValue(APIResponse.loading(requestID));
        loginRepository.toVerifyOtp(otp, mobileNo, this, requestID);
    }

    public void doLoginWithEmail(String email, String password, int type, String socialAccountId, Integer requestID, String deviceId) {
        responseMutableLiveData.setValue(APIResponse.loading(requestID));
        loginRepository.doLoginWithEmail(email, password, type, socialAccountId, this, requestID, deviceId);
    }

    @Override
    public void onSuccess(Object callResponse, Integer requestID) {
        responseMutableLiveData.setValue(APIResponse.success(callResponse, requestID));
    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {
        responseMutableLiveData.setValue(APIResponse.error(error, requestID));

    }
}
