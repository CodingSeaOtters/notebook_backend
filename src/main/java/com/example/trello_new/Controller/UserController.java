package com.example.trello_new.Controller;


import com.example.trello_new.DTOs.UserDto;
import com.example.trello_new.Entities.User;
import com.example.trello_new.JWTValidate;
import com.example.trello_new.Repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "/**" )
@RestController

@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    Gson gson = new GsonBuilder().setLenient().create();
    PasswordEncoder myEncoder = new BCryptPasswordEncoder();

    JWTValidate verifier = new JWTValidate();



   /*   {
    "username" :  "Alfred",
    "password" : "spkdvj"
        }
    */

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody String json){
        JSONObject jo = new JSONObject(json);
        String password = jo.getString("password");
        String passwordEncrypted = myEncoder.encode(password);
        User user = new User(jo.getString("username"), passwordEncrypted);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader)  {
        if(verifier.validateToken(authorizationHeader)){
            Optional<User> user = userRepository.findByUsername(username);
            return user.map(value -> new ResponseEntity<>(makeDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        } else {
            userRepository.deleteById(userId);
        }
    }

    private UserDto makeDto(User requested){
        List<Long> boards = new ArrayList<>();
        if(requested.getUses() != null) {
            requested.getUses().forEach(board -> boards.add(board.getBoardId()));
        }
        return new UserDto(requested.getId(), requested.getUsername(), requested.getPassword(), boards);
    }



}
