package com.build2gether.fx.Chat;

/**
 * Created by abhilashnair on 4/5/16.
 */
public class ChatMessage {
    private String sender;
    private String text;

    public ChatMessage() {
        // necessary for Firebase's deserializer
    }
    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
