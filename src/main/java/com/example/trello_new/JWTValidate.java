package com.example.trello_new;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTValidate {

    private final String issuer = "http://localhost:3000/auth";

    private final String secret = "IchMagKatzen";

    private final Algorithm algorithm = Algorithm.HMAC256(secret);

    private final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build();

    public Boolean validateToken(String header){
        String jwt = header.substring(7);
        DecodedJWT decodedJWT = null;
        try{
            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
        return decodedJWT != null && decodedJWT.getExpiresAt().after(new Date());
    }

}
