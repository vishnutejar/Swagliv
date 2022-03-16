
package com.app.swagliv.model.livestream.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartLiveStreamResp {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("liveStreamId")
        @Expose
        private String liveStreamId;
        @SerializedName("customerId")
        @Expose
        private String customerId;
        @SerializedName("resourceURI")
        @Expose
        private String resourceURI;
        @SerializedName("liveStreamStartedAt")
        @Expose
        private String liveStreamStartedAt;
        @SerializedName("liveStreamStatus")
        @Expose
        private String liveStreamStatus;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getLiveStreamId() {
            return liveStreamId;
        }

        public void setLiveStreamId(String liveStreamId) {
            this.liveStreamId = liveStreamId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getResourceURI() {
            return resourceURI;
        }

        public void setResourceURI(String resourceURI) {
            this.resourceURI = resourceURI;
        }

        public String getLiveStreamStartedAt() {
            return liveStreamStartedAt;
        }

        public void setLiveStreamStartedAt(String liveStreamStartedAt) {
            this.liveStreamStartedAt = liveStreamStartedAt;
        }

        public String getLiveStreamStatus() {
            return liveStreamStatus;
        }

        public void setLiveStreamStatus(String liveStreamStatus) {
            this.liveStreamStatus = liveStreamStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
