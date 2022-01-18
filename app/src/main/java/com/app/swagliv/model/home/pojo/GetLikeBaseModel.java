package com.app.swagliv.model.home.pojo;

import com.app.swagliv.model.login.pojo.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLikeBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("Data")
    @Expose
    private ArrayList<User> peopleLikes = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<User> getPeopleLikes() {
        return peopleLikes;
    }

    public void setPeopleLikes(ArrayList<User> peopleLikes) {
        this.peopleLikes = peopleLikes;
    }


}
