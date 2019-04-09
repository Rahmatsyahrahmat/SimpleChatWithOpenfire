package com.rahmatsyah.simlplechatwithopenfire.model;

public class ChatData {
    private ImageData imageData;
    private MessageData messageData;

    public ChatData(ImageData imageData) {
        this.imageData = imageData;
    }

    public ChatData(MessageData messageData) {
        this.messageData = messageData;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public MessageData getMessageData() {
        return messageData;
    }
}
