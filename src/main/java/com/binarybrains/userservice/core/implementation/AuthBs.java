package com.binarybrains.userservice.core.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.input.AuthService;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthBs implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthProvider authProvider;
    private final ErrorGlobalMapper errorMapper;
    private final UserRepository userRepository;
    @Override
    public Either<ErrorInfo, String> register(Register register) throws AuthenticationException {
        Either<ErrorInfo, String> response = Either.left(errorMapper.getRn003());
        Optional<List<User>> userRegistered = userRepository.findByEmail(register.getEmail());
        if(userRegistered.isPresent() && userRegistered.get().isEmpty()){
            System.out.println(register.getPassword());
            register.setPassword(passwordEncoder.encode(register.getPassword()));
            Optional<User> userSaved = userRepository.save(
                User.builder()
                    .name(register.getName())
                    .email(register.getEmail())
                    .number(register.getNumber())
                    .password(register.getPassword())
                    .build()
            );
            if(userSaved.isPresent()){
                response = Either.right("Bearer " + authProvider.generateToken(userSaved.get().getEmail()));
            }else{
                response = Either.left(errorMapper.getRn000());
            }
        }
        return response;
    }
    
}
