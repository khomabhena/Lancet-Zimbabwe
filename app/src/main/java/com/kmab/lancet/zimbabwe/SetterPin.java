package com.kmab.lancet.zimbabwe;

public class SetterPin {

    public SetterPin() {
    }

    private String adminPin;
    private long timestamp;

    public SetterPin(String adminPin, long timestamp) {
        this.adminPin = adminPin;
        this.timestamp = timestamp;
    }

    public String getAdminPin() {
        return adminPin;
    }

    public void setAdminPin(String adminPin) {
        this.adminPin = adminPin;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
