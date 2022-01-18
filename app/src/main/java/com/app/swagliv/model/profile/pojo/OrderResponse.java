package com.app.swagliv.model.profile.pojo;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("Data")
    private Order order;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order data) {
        this.order = data;
    }
}
