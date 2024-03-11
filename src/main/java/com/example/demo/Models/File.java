package com.example.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "Links")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    private ObjectId id;

    private String title;

    private Binary file;

    private String type;

    public File(String title) {
        this.title = title;
    }

}
