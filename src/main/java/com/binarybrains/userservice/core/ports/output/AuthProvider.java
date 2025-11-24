package com.binarybrains.userservice.core.ports.output;

import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.core.entity.User;

public interface AuthProvider {
    Token generateToken(User user);
    boolean validateToken(String token);
    
}
