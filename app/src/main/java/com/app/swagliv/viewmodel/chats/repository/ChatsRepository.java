package com.app.swagliv.viewmodel.chats.repository;


import android.util.Log;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.chat.api.MessageService;
import com.app.swagliv.model.chat.api.UserChatsService;
import com.app.swagliv.model.chat.pojo.chat.Message;
import com.app.swagliv.model.chat.pojo.chatlist.ChatBaseModel;
import com.app.swagliv.model.chat.pojo.chatlist.MessageBaseModel;
import com.app.swagliv.model.chat.pojo.chatlist.UserChats;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsRepository {

    /*public void getChatList(Context context, String filePath, APIResponseListener apiResponseListener, Integer requestID) {
        String data = Utility.readJsonFromAssets(context, filePath);
        if (data != null) {
            Type chatsType = new TypeToken<ChatBaseModel>() {
            }.getType();
            ChatBaseModel chatBaseModel = new Gson().fromJson(data, chatsType);
            apiResponseListener.onSuccess(chatBaseModel.getChats(), requestID);
        } else
            apiResponseListener.onFailure(new Exception(), requestID);
    }*/

    public void getChatList(APIResponseListener apiResponseListener,Integer requestID) {
        UserChatsService userChatsService = ApplicationRetrofitServices.getInstance().getUserChatsService();
        Call<ChatBaseModel> call = userChatsService.getChatList();
        call.enqueue(new Callback<ChatBaseModel>() {
            @Override
            public void onResponse(Call<ChatBaseModel> call, Response<ChatBaseModel> response) {
                ChatBaseModel chatBaseModel = response.body();
                if(response.isSuccessful() && chatBaseModel != null){
                    if(chatBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE){
                        apiResponseListener.onSuccess(chatBaseModel.getChats(),requestID);
                    }
                }else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<ChatBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);
            }
        });
    }


    public void getPreviousChat(String receiverId, String conversationId, APIResponseListener apiResponseListener,Integer requestID) {
        MessageService messageService = ApplicationRetrofitServices.getInstance().getMessageService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("receiverId", receiverId);
        jsonObject.addProperty("conversationId", conversationId);
        Call<MessageBaseModel> call = messageService.getPreviousChat(jsonObject);
        call.enqueue(new Callback<MessageBaseModel>() {
            @Override
            public void onResponse(Call<MessageBaseModel> call, Response<MessageBaseModel> response) {
                MessageBaseModel messageBaseModel = response.body();
                if(response.isSuccessful() && messageBaseModel != null){
                    if(messageBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE){
                        apiResponseListener.onSuccess(messageBaseModel.getChats(),requestID);
                    }
                }else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<MessageBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(t, requestID);
            }
        });
    }

}
