package com.app.swagliv.viewmodel.searchCrush.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;

public class SearchCrushViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> mutableLiveData;
    private SearchCrushRepository searchCrushRepository;

    public SearchCrushViewModel() {
        mutableLiveData = new MutableLiveData<>();
        searchCrushRepository = new SearchCrushRepository();
    }

    public void getCrushList(Context context, String filePath, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        searchCrushRepository.getCrushList(context, filePath, this, requestID);
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
