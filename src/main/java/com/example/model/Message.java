package com.example.model;

public class Message {

    private String authorUsername;
    private String message;
    private String messageDateTime;

    public Message() {
    }

    public Message(String authorUsername, String message, String messageDateTime) {
        this.authorUsername = authorUsername;
        this.message = message;
        this.messageDateTime = messageDateTime;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
