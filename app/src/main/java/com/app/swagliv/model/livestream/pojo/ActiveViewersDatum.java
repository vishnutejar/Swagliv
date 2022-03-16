
package com.app.swagliv.model.livestream.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveViewersDatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("viewerId")
    @Expose
    private String viewerId;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewerId() {
        return viewerId;
    }

    public void setViewerId(String viewerId) {
        this.viewerId = viewerId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
