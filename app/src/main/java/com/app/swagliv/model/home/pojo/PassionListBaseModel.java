package com.app.swagliv.model.home.pojo;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PassionListBaseModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("passions")
        @Expose
        private ArrayList<Passions> passions = null;

        public ArrayList<Passions> getPassions() {
            return passions;
        }

        public void setPassions(ArrayList<Passions> passions) {
            this.passions = passions;
        }

    }
}
