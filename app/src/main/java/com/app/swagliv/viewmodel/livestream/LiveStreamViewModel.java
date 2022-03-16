package com.app.swagliv.viewmodel.livestream;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.viewmodel.livestream.repository.LivestreamRepository;

public class LiveStreamViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> responseMutableLiveData;
    private LivestreamRepository livestreamRepository;

    public LiveStreamViewModel() {
        responseMutableLiveData = new MutableLiveData<>();
        livestreamRepository = new LivestreamRepository();
    }

    public void postCommentOnLiveStream(String streamId, String comment, Integer reqId) {
        responseMutableLiveData.setValue(APIResponse.loading(reqId));
        livestreamRepository.postCommentOnLiveStream(streamId, comment, this, reqId);
    }

    public void startLiveStream(String resourceURI, Integer reqId) {
        responseMutableLiveData.setValue(APIResponse.loading(reqId));
        livestreamRepository.startLiveStream(resourceURI, this, reqId);
    }

    public void getConnectionsList(Integer reqId) {
        responseMutableLiveData.setValue(APIResponse.loading(reqId));
        livestreamRepository.getConnectionsList(this, reqId);
    }

    public void getAllLiveStreamCommentsResp(String url, Integer reqId) {
        responseMutableLiveData.setValue(APIResponse.loading(reqId));
        livestreamRepository.getAllLiveStreamComments(url, this, reqId);
    }

    public void getListOfActiveViewers(String stremaId, Integer reqId) {
        responseMutableLiveData.setValue(APIResponse.loading(reqId));
        livestreamRepository.getListOfActiveViewers(stremaId, this, reqId);
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
