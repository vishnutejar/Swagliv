package com.app.swagliv.model.chat.pojo.chatlist;

import com.app.swagliv.model.chat.pojo.chat.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageBaseModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ArrayList<Message> chats = null;

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

    public ArrayList<Message> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Message> chats) {
        this.chats = chats;
    }
}
