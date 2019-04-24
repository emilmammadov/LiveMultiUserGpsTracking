package com.livetracking;

public class User {
    String username, pass;

    public User(){

    }

    public User(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }
}
