package com.app.swagliv.receiver.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.app.common.interfaces.OtpReceivedInterface;
import com.app.common.utils.Utility;
import com.app.swagliv.constant.AppInstance;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;


/**
 * This is a receiver class which is read sms which is sent from app. and parse otp and callback otp.
 */
public class OTPReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";
    private OtpReceivedInterface otpReceiveInterface = null;

    public void setOnOtpListeners(OtpReceivedInterface otpReceiveInterface) {
        this.otpReceiveInterface = otpReceiveInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.printLogs(TAG, "OnReceive call");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            otpReceiveInterface = AppInstance.getOtpReceiveInterface();

            switch (mStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Utility.printLogs(TAG, message);
                    if (otpReceiveInterface != null) {
                        int index = message.indexOf(":");
                        String otp = message.substring(index + 2, index + 6);
                        Utility.printLogs(TAG, "parse otp: " + message);
                        otpReceiveInterface.onOtpReceived(otp);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    if (otpReceiveInterface != null) {
                        otpReceiveInterface.onOtpTimeout();
                    }
                    break;
            }
        }
    }
}
