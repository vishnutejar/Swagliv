
package com.app.swagliv.model.livestream.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetListOfActiveViewers {

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
        @SerializedName("views")
        @Expose
        private Integer views;
        @SerializedName("liveStreamStatus")
        @Expose
        private String liveStreamStatus;
        @SerializedName("activeViewersData")
        @Expose
        private List<ActiveViewersDatum> activeViewersData = null;

        public String getLiveStreamId() {
            return liveStreamId;
        }

        public void setLiveStreamId(String liveStreamId) {
            this.liveStreamId = liveStreamId;
        }

        public Integer getViews() {
            return views;
        }

        public void setViews(Integer views) {
            this.views = views;
        }

        public String getLiveStreamStatus() {
            return liveStreamStatus;
        }

        public void setLiveStreamStatus(String liveStreamStatus) {
            this.liveStreamStatus = liveStreamStatus;
        }

        public List<ActiveViewersDatum> getActiveViewersData() {
            return activeViewersData;
        }

        public void setActiveViewersData(List<ActiveViewersDatum> activeViewersData) {
            this.activeViewersData = activeViewersData;
        }

    }

}
