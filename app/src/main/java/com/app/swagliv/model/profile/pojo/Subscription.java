package com.app.swagliv.model.profile.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription implements Parcelable {
    @SerializedName("subscriptionName")
    @Expose
    private String subscriptionName;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("durationInMonths")
    @Expose
    private Integer durationInMonths;
    @SerializedName("pricePerMonth")
    @Expose
    private Integer pricePerMonth;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("subscriptionImageURL")
    @Expose
    private String subscriptionImageURL;
    @SerializedName("purchasedAt")
    @Expose
    private String purchasedAt;
    @SerializedName("planExpiresAt")
    @Expose
    private String planExpiresAt;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static Creator<Subscription> getCREATOR() {
        return CREATOR;
    }

    private boolean isSelected;

    protected Subscription(Parcel in) {
        subscriptionName = in.readString();
        if (in.readByte() == 0) {
            durationInMonths = null;
        } else {
            durationInMonths = in.readInt();
        }
        if (in.readByte() == 0) {
            pricePerMonth = null;
        } else {
            pricePerMonth = in.readInt();
        }
        description = in.readString();
        subscriptionImageURL = in.readString();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public String getSubscriptionImageURL() {
        return subscriptionImageURL;
    }

    public void setSubscriptionImageURL(String subscriptionImageURL) {
        this.subscriptionImageURL = subscriptionImageURL;
    }

    public Integer getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(Integer pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(String purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public String getPlanExpiresAt() {
        return planExpiresAt;
    }

    public void setPlanExpiresAt(String planExpiresAt) {
        this.planExpiresAt = planExpiresAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subscriptionName);
        if (durationInMonths == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(durationInMonths);
        }
        if (pricePerMonth == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pricePerMonth);
        }
        dest.writeString(description);
        dest.writeString(subscriptionImageURL);
    }

}
