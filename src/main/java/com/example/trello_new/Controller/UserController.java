package com.example.trello_new.Controller;

import com.example.trello_new.Entities.User;
import com.example.trello_new.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

   /*   {
    "username" :  "Alfred",
    "password" : "spkdvj"
        }
    */

    @PostMapping("")
    public void createUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/find/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/find/{username}")
    public User getUserByName(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        }
        return user.get();
    }


    @GetMapping("/{userName}")
    public User getUser(@PathVariable String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        }
        return user.get();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        } else {
            userRepository.deleteById(userId);
        }

    }
}
