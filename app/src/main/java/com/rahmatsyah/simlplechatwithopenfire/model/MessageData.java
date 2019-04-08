package com.rahmatsyah.simlplechatwithopenfire.model;

public class MessageData {
    private String heding;
    private String message;

    public MessageData(String heding, String message) {
        this.heding = heding;
        this.message = message;
    }

    public String getHeding() {
        return heding;
    }

    public void setHeding(String heding) {
        this.heding = heding;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
