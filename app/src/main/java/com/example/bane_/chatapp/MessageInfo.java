package com.example.bane_.chatapp;

/**
 * Created by bane- on 4/21/2017.
 */

public class MessageInfo {
    String fromId , text, told;

    public MessageInfo(String fromId, String text, String told) {
        this.fromId = fromId;
        this.text = text;
        this.told = told;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTold() {
        return told;
    }

    public void setTold(String told) {
        this.told = told;
    }
}
