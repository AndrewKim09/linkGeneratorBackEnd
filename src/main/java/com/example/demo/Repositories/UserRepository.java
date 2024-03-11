package com.example.demo.Repositories;

import com.example.demo.Models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    @Query(value = "{'username':?0}")
    Optional<User> getUserByUsername(String username);

    @Query(value = "{'id':?0}")
    Optional<User> getUserById(ObjectId id);
}
