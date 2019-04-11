package com.rahmatsyah.simlplechatwithopenfire.model;

public class ChatData {
    private ImageData imageData;
    private MessageData messageData;
    private FileData fileData;

    public ChatData(ImageData imageData) {
        this.imageData = imageData;
    }

    public ChatData(MessageData messageData) {
        this.messageData = messageData;
    }

    public ChatData(FileData fileData) {
        this.fileData = fileData;
    }

    public FileData getFileData() {
        return fileData;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public MessageData getMessageData() {
        return messageData;
    }
}
