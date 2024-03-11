package com.example.demo.Controllers;

import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<ObjectId> addUser(@RequestParam("username") String username, @RequestParam("password") String password){
        Optional<User> existingUser = findUser(username);

        if(!existingUser.isEmpty()){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(userService.addUser(username, password), HttpStatus.CREATED);
        }
    }


    @GetMapping("/{name}")
    public Optional<User> findUser(@PathVariable(value="name") String username){
        return userService.findUserByName(username);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> checkUser(@RequestBody Map<String,String> data){
        if(!data.containsKey("username") || !data.containsKey("password") ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<User> existingUser = userService.findUserByName(data.get("username"));

        if(existingUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else{
            if(existingUser.get().getPassword().equals(data.get("password"))) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", existingUser.get().getId().toString());
                userMap.put("username", existingUser.get().getUsername());
                userMap.put("files", existingUser.get().getFiles());
                return new ResponseEntity<>(userMap, HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
