package com.app.swagliv.model.livestream;


import android.app.Notification;
import android.nfc.Tag;
import android.util.Log;

import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class OneSignalNotificationSender {
    public static void sendDeviceNotification(final OneSignalNotification notification) {
        new Thread(() -> {
            OSDeviceState deviceState = OneSignal.getDeviceState();
            String userId = deviceState != null ? deviceState.getUserId() : null;
            boolean isSubscribed = deviceState != null && deviceState.isSubscribed();

            if (!isSubscribed)
                return;

            //int pos = notification.getTemplatePos();
            int pos = 1;
            try {
                JSONObject notificationContent = new JSONObject("{'include_player_ids': ['" + userId + "']," +
                        "'headings': {'en': '" + notification.getTitle() + "'}," +
                        "'contents': {'en': '" + notification.getMessage() + "'}," +
                        "'small_icon': '" + notification.getSmallIconRes() + "'," +
                        "'large_icon': '" + "" + "'," +
                        "'big_picture': '" + "" + "'," +
                        "'android_group': '" + "" + "'," +
                        "'buttons': " + notification.getButtons() + "," +
                        "'android_led_color': 'FFE9444E'," +
                        "'android_accent_color': 'FFE9444E'," +
                        "'android_sound': 'nil'}");

                OneSignal.postNotification(notificationContent, new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d("", "Success sending notification: " + response.toString());
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Log.d("", "Failure sending notification: " + response.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
