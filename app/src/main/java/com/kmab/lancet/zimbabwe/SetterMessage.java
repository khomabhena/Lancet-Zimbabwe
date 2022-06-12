package com.kmab.lancet.zimbabwe;

public class SetterMessage {

    public SetterMessage() {
    }

    private String key, uid, message;
    private boolean isSeen, isReplied;
    private long timestamp;

    public SetterMessage(String key, String uid, String message, boolean isSeen, boolean isReplied, long timestamp) {
        this.key = key;
        this.uid = uid;
        this.message = message;
        this.isSeen = isSeen;
        this.isReplied = isReplied;
        this.timestamp = timestamp;
    }

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = replied;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
