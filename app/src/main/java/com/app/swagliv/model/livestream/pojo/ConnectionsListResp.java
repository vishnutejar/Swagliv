
package com.app.swagliv.model.livestream.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionsListResp {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("connectionsCount")
    @Expose
    private Integer connectionsCount;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getConnectionsCount() {
        return connectionsCount;
    }

    public void setConnectionsCount(Integer connectionsCount) {
        this.connectionsCount = connectionsCount;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("Images")
        @Expose
        private Images images;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private String gender;

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public class Images {

            @SerializedName("profileImages")
            @Expose
            private String profileImages;
            @SerializedName("personalImages")
            @Expose
            private List<Object> personalImages = null;

            public String getProfileImages() {
                return profileImages;
            }

            public void setProfileImages(String profileImages) {
                this.profileImages = profileImages;
            }

            public List<Object> getPersonalImages() {
                return personalImages;
            }

            public void setPersonalImages(List<Object> personalImages) {
                this.personalImages = personalImages;
            }

        }


    }

}
