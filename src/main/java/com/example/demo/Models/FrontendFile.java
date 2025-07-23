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

import java.sql.Timestamp;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontendFile {
    @Id
    private String id;

    private String title;

    private Binary file;

    private String type;
    private String email;
    private Date date;

    public FrontendFile(String title) {
        this.title = title;
    }

    public FrontendFile(File file)
    {
        this.id = file.getId().toHexString();
        this.title = file.getTitle();
        this.file = file.getFile();
        this.type = file.getType();
        this.email = file.getEmail();
        this.date = file.getId().getDate();
    }



}
