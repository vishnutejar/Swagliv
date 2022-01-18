package com.app.swagliv.model.profile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class SubscriptionBaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ArrayList<Subscription> subscriptionPlans = null;

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

    public ArrayList<Subscription> getSubscriptionPlans() {
        return subscriptionPlans;
    }

    public void setSubscriptionPlans(ArrayList<Subscription> subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }


}
