package com.binarybrains.userservice.core.entity;

import com.binarybrains.userservice.utils.enums.TokenType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Token {
    public Long id; 
    public String token;
    public TokenType tokenType;
    public boolean revoked;
    public boolean expired;
    public User user; 
}
