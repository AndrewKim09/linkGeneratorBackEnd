package com.example.demo.Repositories;

import com.example.demo.Models.File;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, ObjectId> {
    @Query(value = "{'title':?0}")
    Optional<File> getFileByTitle(String name);

    @Query(value = "{'id':?0}")
    Optional<File> getFileById(ObjectId id);


}
