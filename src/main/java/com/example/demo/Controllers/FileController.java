package com.example.demo.Controllers;

import com.example.demo.Models.File;
import com.example.demo.Models.FrontendFile;
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
import java.sql.Array;
import java.util.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    UserController userController;

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseEntity test(){
        return new ResponseEntity("hello", HttpStatus.OK);
    }

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
    public ResponseEntity<List<FrontendFile>> getFilesByUser(@RequestParam("email") String email){
            System.out.println(email);

            List<File> userFiles = fileService.getAllByEmail(email);
            List<FrontendFile> frontendUserFiles = new ArrayList<>();

            for(int i = 0; i < userFiles.size(); i++)
            {
                FrontendFile file = new FrontendFile(userFiles.get(i));
                frontendUserFiles.add(file);
            }

            return new ResponseEntity<>(frontendUserFiles, HttpStatus.OK);

    }



    @PostMapping("/add")
    public ResponseEntity<ObjectId> addFile(@RequestParam("title") String title, @RequestParam("file") MultipartFile file, Model model, @RequestParam("id") String email) throws IOException {
        String contentType = file.getContentType();
        String type = "";

        if(email.isBlank()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(contentType != null){
            type = contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ObjectId id = fileService.addFile(title, type, file, email);
        return new ResponseEntity<ObjectId>(id, HttpStatus.CREATED);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileId")String id){
        ObjectId objectId = new ObjectId(id);
        Optional<File>existingFile = fileService.getById(objectId);

        if(existingFile.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else{
            fileService.deleteById(objectId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }



}
