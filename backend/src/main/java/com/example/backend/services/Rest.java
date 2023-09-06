package com.example.backend.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.app.service.CustomVision;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class Rest {
    private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=luca88;AccountKey=0ceUgK6VBCn6vhX/C39rBaF6rPpM+3Th68OaVscXXfNobgrsngyNFJF3X/GAkrpCeJznJhVXCPNG+AStLLd41Q==;EndpointSuffix=core.windows.net";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }



    @PostMapping("/images")
    public ResponseEntity<Object> uploadImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        saveImageToFile(rawBytes, imageName);
        saveToCloud(rawBytes, imageName);
        CustomVision.uploadImage(CustomVision.tags.get("yifan"), rawBytes);
        return new ResponseEntity<>("Successfully uploaded image", HttpStatus.OK);
    }


    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException { 
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        return CustomVision.validate(rawBytes);
    }


    private void saveToCloud(byte[] image, String imageName) {
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(CONNECTION_STRING).buildClient();

        // Create a unique name for the container
        String containerName = "images";

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName);
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }

    
}






