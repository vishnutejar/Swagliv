package com.app.swagliv.model.chat.api;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.chat.pojo.chatlist.MessageBaseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageService {

    @POST(AppConstant.CHAT.GET_PREVIOUS_CHATS)
    Call<MessageBaseModel> getPreviousChat(@Body JsonObject jsonObject);
}
