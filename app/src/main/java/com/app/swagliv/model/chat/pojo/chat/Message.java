package com.app.swagliv.model.chat.pojo.chat;

public class Message {
    private String message;
    private long time;
    private String messageId;
    private int messageReceivedCode;// 0 -> left message , 1-> right message
    private int messageType;// 0 -> left message , 1-> right message

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
