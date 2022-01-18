package com.app.swagliv.model.Payment.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderVerifyBaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("razorpaySignature")
    @Expose
    private String razorpaySignature;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
