package com.app.swagliv.model.chat.api;

import android.util.Log;

import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.chat.pojo.chatlist.ChatBaseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserChatsService {
    @GET(AppConstant.CHAT.GET_USER_CHATS)
    Call<ChatBaseModel> getChatList();
}
