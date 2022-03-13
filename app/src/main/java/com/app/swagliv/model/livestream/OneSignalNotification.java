package com.app.swagliv.model.livestream;

public class OneSignalNotification {
    public String getTitle() {
        return title;
    }

    public String[][] getData() {
        return data;
    }

    public String getSmallIconRes() {
        return smallIconRes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getButtons() {
        return buttons;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }

    public int getPos() {
        return pos;
    }

    private String title;
    private String[][] data;
    private String smallIconRes;
    private String iconUrl;

    public String getMessage() {
        return Message;
    }

    private String Message;
    private String buttons;
    private boolean shouldShow;
    private int pos;

    public OneSignalNotification() {
    }

    public OneSignalNotification(String title, String[][] data, String smallIconRes, String iconUrl, String message, String buttons, boolean shouldShow, int pos) {
        this.title = title;
        this.data = data;
        this.smallIconRes = smallIconRes;
        this.iconUrl = iconUrl;
        Message = message;
        this.buttons = buttons;
        this.shouldShow = shouldShow;
        this.pos = pos;
    }
}
