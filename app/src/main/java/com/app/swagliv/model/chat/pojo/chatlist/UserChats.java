package com.app.swagliv.model.chat.pojo.chatlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserChats {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("lastMessage")
    @Expose
    private String lastMessage;
    @SerializedName("profileImages")
    @Expose
    private String profileImage;
    @SerializedName("time")
    @Expose
    private String timeSpan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfileImage() {
        if (profileImage == null) {
            return "https://www.pngfind.com/pngs/m/676-6764065_default-profile-picture-transparent-hd-png-download.png";
        }
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }


}
