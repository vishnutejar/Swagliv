package com.app.swagliv.model.home.pojo;


import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("Data")
    @Expose
    private Data dataModel = null;

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

    public Data getDataModel() {
        return dataModel;
    }

    public void setDataModel(Data dataModel) {
        this.dataModel = dataModel;
    }

    public class Data {
        @SerializedName("currentSubscribedPlan")
        @Expose
        private Subscription currentSubscribedPlan;
        @SerializedName("profiles")
        @Expose
        private ArrayList<User> profiles = null;
        @SerializedName("matchedProfiles")
        @Expose
        private ArrayList<User> matchedProfile = null;

        public Subscription getCurrentSubscribedPlan() {
            return currentSubscribedPlan;
        }

        public void setCurrentSubscribedPlan(Subscription currentSubscribedPlan) {
            this.currentSubscribedPlan = currentSubscribedPlan;
        }

        public ArrayList<User> getProfiles() {
            return profiles;
        }

        public void setProfiles(ArrayList<User> profiles) {
            this.profiles = profiles;
        }

        public ArrayList<User> getMatchedProfile() {
            return matchedProfile;
        }

        public void setMatchedProfile(ArrayList<User> matchedProfile) {
            this.matchedProfile = matchedProfile;
        }

    }

}
