package com.vrgc.eguidance.Model;

public class MessageModel {
    public String sender;
    public String text;
    public long timestamp;

    public MessageModel() {}

    public MessageModel(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }
}

