package com.example.bane_.chatapp;

/**
 * Created by Interns on 4/19/2017.
 */

public class MessageInfo {

    private String fromId;
    private String text;
    private String timestamp;
    private String told;



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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTold() {
        return told;
    }

    public void setTold(String told) {
        this.told = told;
    }
}