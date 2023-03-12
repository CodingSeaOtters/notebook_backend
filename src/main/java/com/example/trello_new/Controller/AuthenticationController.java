package com.example.trello_new.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.trello_new.Entities.User;
import com.example.trello_new.JWTValidate;
import com.example.trello_new.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;
    PasswordEncoder myEncoder = new BCryptPasswordEncoder();

    private final String secret = "IchMagKatzen";
    private String issuer = "http://localhost:3000/auth";

    Algorithm algorithm = Algorithm.HMAC256(secret);

    JWTValidate validate = new JWTValidate();




    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String json) {
        JSONObject jo = new JSONObject(json);
        Optional<User> user = userRepository.findByUsername(jo.getString("username"));

        if (user.isPresent()) {
            User gottenUser = user.get();
            String password = gottenUser.getPassword();

            if (myEncoder.matches(jo.getString("password"), password)) {
                String jwtToken = JWT.create()
                        .withIssuer(issuer)
                        .withSubject(user.get().getId().toString())
                        .withClaim("username", user.get().getUsername())
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60_000)))
                        .sign(algorithm);

                return new ResponseEntity<>(jwtToken, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("refresh/{userId}")
    public ResponseEntity<?> onRefresh(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader){
        if(validate.validateToken(authorizationHeader)){
            Optional<User> user = userRepository.findById(userId);
            return new ResponseEntity<>(user.get(), HttpStatus.ACCEPTED);
        } else if(validate.isExpired(authorizationHeader)){
            Optional<User> user = userRepository.findById(userId);
            String jwtToken = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.get().getId().toString())
                    .withClaim("username", user.get().getUsername())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60_000)))
                    .sign(algorithm);
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}


