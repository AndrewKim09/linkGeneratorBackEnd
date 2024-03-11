package com.example.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    private String username;

    private String password;

    private List<String> files;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.files = new ArrayList<>();
    }

    public void addFile(String id){
        this.files.add(id);
    }

    public void removeFile(String id) {
        // Use an Iterator to safely remove elements while iterating
        Iterator<String> iterator = files.iterator();
        while (iterator.hasNext()) {
            String fileId = iterator.next();
            if (fileId.equals(id)) {
                iterator.remove();
                break; // Assuming each file ID is unique, exit loop once found
            }
        }
    }

}
