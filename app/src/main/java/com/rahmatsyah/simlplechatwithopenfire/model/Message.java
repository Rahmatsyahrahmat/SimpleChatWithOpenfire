package com.rahmatsyah.simlplechatwithopenfire.model;

public class Message {
    private String message;
    private User sender;

    public Message(String message, User sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }
}
