package com.app.swagliv.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Advertise implements Parcelable {

    private String title;
    private String text;
    private String messagetxt;

    public Advertise(String title, String text, String messagetxt) {
        this.title = title;
        this.text = text;
        this.messagetxt = messagetxt;
    }

    protected Advertise(Parcel in) {
        title = in.readString();
        text = in.readString();
        messagetxt = in.readString();
    }

    public static final Creator<Advertise> CREATOR = new Creator<Advertise>() {
        @Override
        public Advertise createFromParcel(Parcel in) {
            return new Advertise(in);
        }

        @Override
        public Advertise[] newArray(int size) {
            return new Advertise[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessagetxt() {
        return messagetxt;
    }

    public void setMessagetxt(String messagetxt) {
        this.messagetxt = messagetxt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(messagetxt);
    }
}
