package com.app.swagliv.model.Payment.pojo;

import com.app.swagliv.model.profile.pojo.Subscription;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderCreatedBaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Data")
    @Expose
    private Subscription orderData;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Subscription getOrderData() {
        return orderData;
    }

    public void setOrderData(Subscription orderData) {
        this.orderData = orderData;
    }


}
