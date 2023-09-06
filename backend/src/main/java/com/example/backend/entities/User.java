package com.example.backend.entities;

public class User {

    private String userID;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.userID = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "Person[id='%s', fullname='%s', email='%s']",
                userID, name, email);
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
