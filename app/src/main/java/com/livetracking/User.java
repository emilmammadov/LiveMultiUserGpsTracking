package com.livetracking;

public class User {
    String userId, username, password;

    public User(){

    }

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public String getUserId(){
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
