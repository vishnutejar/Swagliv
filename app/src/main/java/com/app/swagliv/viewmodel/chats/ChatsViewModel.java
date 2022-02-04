package com.app.swagliv.viewmodel.chats;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.viewmodel.chats.repository.ChatsRepository;

public class ChatsViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> mutableLiveData;
    private ChatsRepository chatsRepository;

    public ChatsViewModel() {
        mutableLiveData = new MutableLiveData<>();
        chatsRepository = new ChatsRepository();
    }


    public void getChatList(Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        chatsRepository.getChatList(this, requestID);
    }

    public void getPreviousChat(String receiverId, String conversationId, Integer requestID) {
        mutableLiveData.setValue(APIResponse.loading(requestID));
        chatsRepository.getPreviousChat(receiverId, conversationId,this, requestID);
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
