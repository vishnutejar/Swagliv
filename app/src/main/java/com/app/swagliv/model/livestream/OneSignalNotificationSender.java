package com.app.swagliv.model.livestream;

import android.util.Log;

import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class OneSignalNotificationSender{
/*
    public static void sendDeviceNotification(final Notification notification) {
        new Thread(() -> {
            OSDeviceState deviceState = OneSignal.getDeviceState();
            String userId = deviceState != null ? deviceState.getUserId() : null;
            boolean isSubscribed = deviceState != null && deviceState.isSubscribed();

            if (!isSubscribed)
                return;

            int pos = notification.getTemplatePos();
            try {
                JSONObject notificationContent = new JSONObject("");

                OneSignal.postNotification(notificationContent, new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(Tag.DEBUG, "Success sending notification: " + response.toString());
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Log.d(Tag.ERROR, "Failure sending notification: " + response.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
*/
}
