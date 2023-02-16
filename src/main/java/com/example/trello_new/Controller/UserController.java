package com.example.trello_new.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.trello_new.DTOs.UserDto;
import com.example.trello_new.Entities.User;
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

    private String issuer = "http://localhost:3000/auth";

    private final String secret = "IchMagKatzen";

    private Algorithm algorithm = Algorithm.HMAC256(secret);

    private JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build();


   /*   {
    "username" :  "Alfred",
    "password" : "spkdvj"
        }
    */

    @PostMapping("")
    public void createUser(@RequestBody String json){
        JSONObject jo = new JSONObject(json);
        String password = jo.getString("password");
        String passwordEncrypted = myEncoder.encode(password);
        User user = new User(jo.getString("username"), passwordEncrypted);
        userRepository.save(user);
    }

    @GetMapping("/find/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader)  {
        if(validateToken(authorizationHeader, username)){
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
        UserDto sendUser = new UserDto(requested.getId(), requested.getUsername(), requested.getPassword(), boards);
        return sendUser;
    }

    private Boolean validateToken(String header, String username){
        String jwt = header.substring(7);
        DecodedJWT decodedJWT = null;
        try{
            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
        return decodedJWT != null;
    }
}
