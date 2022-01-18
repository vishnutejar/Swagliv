package com.app.swagliv.model;

import com.app.swagliv.model.login.pojo.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CrushSearch {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ArrayList<User> crushData;

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

    public ArrayList<User> getCrushData() {
        return crushData;
    }

    public void setCrushData(ArrayList<User> crushData) {
        this.crushData = crushData;
    }


}
