package com.app.swagliv.model.home.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Passions implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("label")
    @Expose
    private String label;

    protected Passions(Parcel in) {
        id = in.readString();
        label = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Passions> CREATOR = new Creator<Passions>() {
        @Override
        public Passions createFromParcel(Parcel in) {
            return new Passions(in);
        }

        @Override
        public Passions[] newArray(int size) {
            return new Passions[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}

