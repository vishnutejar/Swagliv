package com.app.swagliv.model.login.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.swagliv.model.home.pojo.Passions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User  implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("statusId")
    @Expose
    private Integer statusId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("passions")
    @Expose
    private ArrayList<Passions> passions;
    @SerializedName("personalImages")
    @Expose
    private ArrayList<String> personalImage;
    @SerializedName("profileImages")
    @Expose
    private String profileImages;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobileNumber")
    @Expose
    private String contactNumber;
    @SerializedName("userAge")
    @Expose
    private int userAge;
    private String password;
    private String aboutMe;

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        if (in.readByte() == 0) {
            statusId = null;
        } else {
            statusId = in.readInt();
        }
        dob = in.readString();
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        id = in.readString();
        passions = in.createTypedArrayList(Passions.CREATOR);
        personalImage = in.createStringArrayList();
        profileImages = in.readString();
        token = in.readString();
        gender = in.readString();
        contactNumber = in.readString();
        userAge = in.readInt();
        password = in.readString();
        aboutMe = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {

    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public ArrayList<Passions> getPassions() {
        return passions;
    }

    public void setPassions(ArrayList<Passions> passions) {
        this.passions = passions;
    }

    public ArrayList<String> getPersonalImage() {
        return personalImage;
    }

    public void setPersonalImage(ArrayList<String> personalImage) {
        this.personalImage = personalImage;
    }

    public String getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(String profileImages) {
        this.profileImages = profileImages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        if (statusId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(statusId);
        }
        dest.writeString(dob);
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(type);
        }
        dest.writeString(id);
        dest.writeTypedList(passions);
        dest.writeStringList(personalImage);
        dest.writeString(profileImages);
        dest.writeString(token);
        dest.writeString(gender);
        dest.writeString(contactNumber);
        dest.writeInt(userAge);
        dest.writeString(password);
        dest.writeString(aboutMe);
    }
}
