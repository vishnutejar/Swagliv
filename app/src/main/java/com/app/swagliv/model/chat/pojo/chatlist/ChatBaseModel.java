package com.app.swagliv.model.chat.pojo.chatlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatBaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ArrayList<UserChats> Chats = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserChats> getChats() {
        return Chats;
    }

    public void setChats(ArrayList<UserChats> chats) {
        Chats = chats;
    }

}
