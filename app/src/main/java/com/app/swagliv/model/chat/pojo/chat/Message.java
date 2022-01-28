package com.app.swagliv.model.chat.pojo.chat;

public class Message {
    private int status;
    private String message;
    private String conversationId;
    private String senderID;
    private String _id;
    private long time;
    private String messageId;
    private int messageReceivedCode;// 0 -> left message , 1-> right message
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

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getMessageReceivedCode() {
        return messageReceivedCode;
    }

    public void setMessageReceivedCode(int messageSenderID) {
        this.messageReceivedCode = messageSenderID;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", time=" + time +
                ", messageId='" + messageId + '\'' +
                ", messageReceivedCode=" + messageReceivedCode +
                ", messageType=" + messageType +
                '}';
    }
}
