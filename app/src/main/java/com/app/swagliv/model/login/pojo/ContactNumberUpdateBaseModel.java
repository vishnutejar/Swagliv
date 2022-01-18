package com.app.swagliv.model.login.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactNumberUpdateBaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("mobileOtp")
    @Expose
    private String mobileOTP;
    @SerializedName("isOtpSent")
    @Expose
    private Boolean isOTPSent;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMobileOTP() {
        return mobileOTP;
    }

    public void setMobileOTP(String mobileOTP) {
        this.mobileOTP = mobileOTP;
    }

    public Boolean getOTPSent() {
        return isOTPSent;
    }

    public void setOTPSent(Boolean OTPSent) {
        isOTPSent = OTPSent;
    }
}
