package com.binarybrains.userservice.core.ports.output;

public interface AuthProvider {
    String generateToken(String userId);
    boolean validateToken(String token);
    
}
