package com.binarybrains.userservice.core.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.input.AuthService;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.infrastructure.jpa.entity.TokenJpa;
import com.binarybrains.userservice.infrastructure.jpa.repository.TokenJpaRepository;
import com.binarybrains.userservice.utils.enums.TokenType;
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
    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public Either<ErrorInfo, String> register(Register register) throws AuthenticationException {
        Either<ErrorInfo, String> response = Either.left(errorMapper.getRn003());
        Optional<List<User>> userRegistered = userRepository.findByEmail(register.getEmail());
        if(userRegistered.isPresent() && userRegistered.get().isEmpty()){
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
                var token = authProvider.generateToken(userSaved.get().getEmail());
                saveUserToken(userSaved.get(), token);
                response = Either.right(token);
            }else{
                response = Either.left(errorMapper.getRn000());
            }
        }
        return response;
    }
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenJpaRepository.save(TokenJpa.fromEntity(token));
    }
    
}
