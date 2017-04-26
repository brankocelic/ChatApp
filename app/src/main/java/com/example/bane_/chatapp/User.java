package com.example.bane_.chatapp;

/**
 * Created by bane- on 4/25/2017.
 */

public class User {
    public static String id;
    public static  String email;
    public static  String name;

    public User()
    {
    }

    public User(String userName, String eMail) {
        this.name = userName;
        this.email = eMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
