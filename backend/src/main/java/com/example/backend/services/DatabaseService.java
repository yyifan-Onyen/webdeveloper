package com.example.backend.services;

import com.example.backend.dao.DbRepository;
import com.example.backend.entities.Image;
import com.example.backend.entities.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@CrossOrigin(origins = "${FRONTEND_HOST:*}")
@RestController
public class DatabaseService {
    @Autowired
    DbRepository sqlDb;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<String> postForm(@RequestBody String data) throws JSONException {
        JSONObject jsonObject= new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();
        User user = new User(userId, name, email);
        Image image = new Image(userId, rawBytes);
        sqlDb.saveUser(user);
        sqlDb.saveImage(image);
        ResponseEntity<String> result = new ResponseEntity<String>(userId,HttpStatus.OK);
        return result;
    }

    @GetMapping("/image/{userId}")
    ResponseEntity<Image> getImage(@PathVariable String userId) throws IOException {
    // try{
        Image image = sqlDb.getImage(userId);
        String imageName = image.getUserID() + ".png";
        saveImageToFile(image.getImageData(), imageName);
        return new ResponseEntity<Image>(image, HttpStatus.OK);
    // }catch(EmptyResultDataAccessException e){
    //      System.out.println("No image found for user " + e.getMessage());
    //      return new ResponseEntity<Image>(HttpStatus.NOT_FOUND);
    // }
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File dir = new File("./images/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }
}

