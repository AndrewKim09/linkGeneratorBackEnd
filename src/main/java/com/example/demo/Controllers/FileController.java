package com.example.demo.Controllers;

import com.example.demo.Models.File;
import com.example.demo.Models.User;
import com.example.demo.Services.FileService;
import com.example.demo.Services.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.print.attribute.standard.Media;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    UserController userController;

    @Autowired
    UserService userService;

    @GetMapping("/{name}")
    public ResponseEntity<File> getFileByName(@PathVariable(value="name") String name){
        Optional<File> file = fileService.getByTitle(name);


        if(file.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            return ResponseEntity.ok(file.get());
        }

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") String id){
        ObjectId objectId = new ObjectId(id);
        Optional<File> file = fileService.getById(objectId);

        if(file.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else{
            byte[] binaryData;
            binaryData = file.get().getFile().getData();

            Resource resource = new InputStreamResource(new ByteArrayInputStream(binaryData));

            HttpHeaders headers = new HttpHeaders();

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.get().getTitle());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(binaryData.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);


        }

    }

    @PostMapping("/all")
    public ResponseEntity<List<File>> getFilesByUser(@RequestParam("files") List<ObjectId> ids){
             List<File> userFiles = new ArrayList<>();

            for(ObjectId fileId : ids){
                Optional<File> file = fileService.getById(fileId);
                userFiles.add(file.get());
            }

            return new ResponseEntity<>(userFiles, HttpStatus.OK);


    }



    @PostMapping("/add")
    public ResponseEntity<ObjectId> addFile(@RequestParam("title") String title, @RequestParam("file") MultipartFile file, Model model, @RequestParam("id") ObjectId userId) throws IOException {
        String contentType = file.getContentType();
        Optional<User> user = userService.findUserById(userId);
        String type = "";

        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);

        if(contentType != null){
            type = contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ObjectId id = fileService.addFile(title, type, file);
        user.get().addFile(id.toString());
        userService.saveUser(user.get());
        return new ResponseEntity<ObjectId>(id, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileId")String id, @PathVariable("userId") String userId){
        ObjectId objectId = new ObjectId(id);
        ObjectId userObjectId = new ObjectId(userId);
        Optional<File>existingFile = fileService.getById(objectId);
        Optional<User>existingUser = userService.findUserById(userObjectId);

        if(existingFile.isEmpty() || existingUser.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else{
            fileService.deleteById(objectId);
            existingUser.get().removeFile(id);
            userService.saveUser(existingUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }



}
