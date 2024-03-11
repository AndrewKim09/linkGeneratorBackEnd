package com.example.demo.Services;

import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> findUserByName(String username){
        return userRepository.getUserByUsername(username);
    }

    public Optional<User> findUserById(ObjectId id){
        return userRepository.findById(id);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }


    public ObjectId addUser(String username, String password){
        return userRepository.insert(new User(username, password)).getId();
    }
}
