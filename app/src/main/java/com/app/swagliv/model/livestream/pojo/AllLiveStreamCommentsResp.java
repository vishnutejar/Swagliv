
package com.app.swagliv.model.livestream.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AllLiveStreamCommentsResp {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("commentsCount")
    @Expose
    private Integer commentsCount;
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

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }



    public class Datum {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("liveStreamId")
        @Expose
        private String liveStreamId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("commentBy")
        @Expose
        private String commentBy;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("customerProfileImage")
        @Expose
        private String customerProfileImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLiveStreamId() {
            return liveStreamId;
        }

        public void setLiveStreamId(String liveStreamId) {
            this.liveStreamId = liveStreamId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommentBy() {
            return commentBy;
        }

        public void setCommentBy(String commentBy) {
            this.commentBy = commentBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCustomerProfileImage() {
            return customerProfileImage;
        }

        public void setCustomerProfileImage(String customerProfileImage) {
            this.customerProfileImage = customerProfileImage;
        }

    }

}
