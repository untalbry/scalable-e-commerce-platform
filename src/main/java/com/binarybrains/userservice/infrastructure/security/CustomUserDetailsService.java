package com.binarybrains.userservice.infrastructure.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.output.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements  UserDetailsService{

    private final UserRepository usernameRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<List<User>> userRegistered = usernameRepository.findByEmail(username);
        if (!userRegistered.isPresent() || userRegistered.get().isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new CustomUserDetails(userRegistered.get().get(0));
    }

}
