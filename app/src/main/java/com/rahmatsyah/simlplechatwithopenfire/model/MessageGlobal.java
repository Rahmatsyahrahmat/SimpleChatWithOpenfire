package com.rahmatsyah.simlplechatwithopenfire.model;

public class MessageGlobal {
    private String heding;
    private String message;

    public MessageGlobal(String heding, String message) {
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
