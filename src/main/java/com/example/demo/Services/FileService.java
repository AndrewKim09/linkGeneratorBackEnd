package com.example.demo.Services;

import com.example.demo.Models.File;
import com.example.demo.Repositories.FileRepository;
import com.fasterxml.jackson.databind.util.NativeImageUtil;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public Optional<File> getByTitle(String name){
        return fileRepository.getFileByTitle(name);
    }

    public Optional<File> getById(ObjectId id){return fileRepository.getFileById(id);}


    public ObjectId addFile(String title, String type, MultipartFile file, String email) throws IOException{
        File newFile = new File(title);
        newFile.setType(type);
        newFile.setFile(
                new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        newFile.setEmail(email);
        newFile = fileRepository.insert(newFile);
        return newFile.getId();

    }

    public List<File> getAllByEmail(String email){return fileRepository.findAllByEmail(email);}

    public void deleteById(ObjectId id){
        fileRepository.deleteById(id);
    }

}
