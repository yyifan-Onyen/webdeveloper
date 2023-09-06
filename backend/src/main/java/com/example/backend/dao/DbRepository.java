package com.example.backend.dao;
import com.example.backend.entities.Image;
import com.example.backend.entities.User;

public interface DbRepository{
    int saveUser(User user);
    int saveImage(Image image);
    User getUser(String userId);
    Image getImage(String userId);
}