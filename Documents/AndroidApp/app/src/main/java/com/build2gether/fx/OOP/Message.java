package com.build2gether.fx.OOP;

import java.lang.ref.SoftReference;

/**
 * Created by adityamittal on 4/6/16.
 */
public class Message {

    private String sender;
    private String text;

    public Message(String sender, String text){
        this.sender = sender;
        this.text = text;
    }

    public Message(){}

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
