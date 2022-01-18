package com.app.swagliv.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PriceModel implements Parcelable {
    private String month_no;
    private String month;
    private String price;

    private boolean isSelected;

    public PriceModel(String month_no, String month, String price) {
        this.month_no = month_no;
        this.month = month;
        this.price = price;
    }


    protected PriceModel(Parcel in) {
        month_no = in.readString();
        month = in.readString();
        price = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<PriceModel> CREATOR = new Creator<PriceModel>() {
        @Override
        public PriceModel createFromParcel(Parcel in) {
            return new PriceModel(in);
        }

        @Override
        public PriceModel[] newArray(int size) {
            return new PriceModel[size];
        }
    };

    public String getMonth_no() {
        return month_no;
    }

    public void setMonth_no(String month_no) {
        this.month_no = month_no;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(month_no);
        dest.writeString(month);
        dest.writeString(price);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
