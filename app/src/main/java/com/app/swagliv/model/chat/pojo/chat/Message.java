package com.app.swagliv.model.chat.pojo.chat;

public class Message {
    private int status;
    private String message;
    private String conversationId;
    private String senderId;
    private String receiverId;
    private String time;
    private String messageId;
    private int messageType;// 0 -> left message , 1-> right message

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", senderID='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", time='" + time + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
