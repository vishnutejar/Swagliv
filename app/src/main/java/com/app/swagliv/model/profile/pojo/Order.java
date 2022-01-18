package com.app.swagliv.model.profile.pojo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {

    @SerializedName("subscriptionName")
    private String subscriptionName;
    @SerializedName("purchaseDate")
    private String purchaseDate;
    @SerializedName("durationInMonths")
    private String durationInMonths;
    @SerializedName("subscriptionId")
    private String subscriptionId;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("currency")
    private String currency;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("_id")
    private String id;
    @SerializedName("amount")
    private double amount;

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(String durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("order_id", getOrderId());
            jo.put("amount", getAmount());
            jo.put("amount_paid", getAmount());
            jo.put("currency", getCurrency());
            jo.put("receipt", getId());
            jo.put("offer_id", getSubscriptionId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo;
    }
}
