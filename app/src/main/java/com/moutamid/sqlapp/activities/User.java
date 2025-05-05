package com.moutamid.sqlapp.activities;

public class User {
    public String name, email;
    public boolean vip;
    public User(String name, String email, boolean vip) {
        this.name = name;
        this.email = email;
        this.vip = vip;
    }
}
