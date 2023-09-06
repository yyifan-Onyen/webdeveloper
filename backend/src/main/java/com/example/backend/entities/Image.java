package com.example.backend.entities;

public class Image {

    private String userID;
    private byte[] imageData;


    public Image(String id, byte[] image) {
        this.userID = id;
        this.imageData = image;
    }

    public String getUserID() {
        return userID;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setImageData(byte[] image) {
        this.imageData = image;
    }
}