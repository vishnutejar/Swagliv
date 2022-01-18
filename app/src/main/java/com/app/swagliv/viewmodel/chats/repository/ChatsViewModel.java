package com.app.swagliv.viewmodel.chats.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;

public class ChatsViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> mutableLiveData;
    private ChatsRepository chatsRepository;

    public ChatsViewModel() {
        mutableLiveData = new MutableLiveData<>();
        chatsRepository = new ChatsRepository();
    }


    public void getChatList(Context context, String filePath, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        chatsRepository.getChatList(context, filePath, this, requestID);
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
