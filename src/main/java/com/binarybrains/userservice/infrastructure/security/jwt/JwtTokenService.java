package com.binarybrains.userservice.infrastructure.security.jwt;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.utils.enums.TokenType;

@Component
public class JwtTokenService implements AuthProvider{
    private final Algorithm ALGORITHM;
    public JwtTokenService(@Value("${jwt.secret-key}") String secretKey) {
        if(secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("JWT secret key must not be null or empty");
        }
        this.ALGORITHM = Algorithm.HMAC256(secretKey); 
    }
    @Override
    public Token generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TimeUnit.DAYS.toMillis(15));

        Token token = Token.builder()
            .user(user)
            .token(JWT.create()
                .withSubject(user.getEmail())
                .withIssuer("binary-brains")
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(this.ALGORITHM))
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        return token; 
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
