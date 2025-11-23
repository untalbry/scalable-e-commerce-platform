package com.binarybrains.userservice.core.entity;

public class Token {
    public enum TokenType {
        BEARER
    }
    public Long id; 
    public String token;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    public String email; 
}
