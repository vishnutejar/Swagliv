package com.app.swagliv.viewmodel.livestream;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.viewmodel.livestream.repository.LivestreamRepository;
import com.app.swagliv.viewmodel.login.repository.LoginRepository;

public class LiveStreamViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> responseMutableLiveData;
    private LivestreamRepository livestreamRepository;

    public  LiveStreamViewModel(){
        responseMutableLiveData = new MutableLiveData<>();
        livestreamRepository=new LivestreamRepository();
    }
    @Override
    public void onSuccess(Object callResponse, Integer requestID) {

    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {

    }
}
