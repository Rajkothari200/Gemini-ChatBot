package com.example.assignment1c030RajKothari;

public class Message {
    private long id;
    private boolean isUser;
    private String text;
    private long timestamp;

    public Message(long id, boolean isUser, String text, long timestamp) {
        this.id = id;
        this.isUser = isUser;
        this.text = text;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public boolean isUser() {
        return isUser;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
