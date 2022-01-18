package com.app.swagliv.viewmodel.chats.repository;

import android.content.Context;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.chat.pojo.ChatBaseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ChatsRepository {

    public void getChatList(Context context, String filePath, APIResponseListener apiResponseListener, Integer requestID) {
        String data = Utility.readJsonFromAssets(context, filePath);
        if (data != null) {
            Type chatsType = new TypeToken<ChatBaseModel>() {
            }.getType();
            ChatBaseModel chatBaseModel = new Gson().fromJson(data, chatsType);
            apiResponseListener.onSuccess(chatBaseModel.getChats(), requestID);
        } else
            apiResponseListener.onFailure(new Exception(), requestID);
    }
}
