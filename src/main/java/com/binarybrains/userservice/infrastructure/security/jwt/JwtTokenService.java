package com.binarybrains.userservice.infrastructure.security.jwt;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.binarybrains.userservice.core.ports.output.AuthProvider;

@Component
public class JwtTokenService implements AuthProvider{
    private final Algorithm ALGORITHM;
    public JwtTokenService(@Value("${jwt.secret-key}") String secretKey) {
        this.ALGORITHM = Algorithm.HMAC256(secretKey); 
    }
    @Override
    public String generateToken(String userEmail) {
       return JWT.create()
            .withSubject(userEmail)
            .withIssuer("binary-brains")
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
            .sign(this.ALGORITHM); 
    }
    @Override
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(this.ALGORITHM).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }    
}
