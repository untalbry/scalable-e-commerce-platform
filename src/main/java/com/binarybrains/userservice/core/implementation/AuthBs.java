package com.binarybrains.userservice.core.implementation;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.Login;
import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.entity.UserToken;
import com.binarybrains.userservice.core.ports.input.AuthService;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.core.ports.output.UserTokenRepository;
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
    private final JavaMailSender mailSender;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final UserTokenRepository userTokenRepository;
    @Override
    public Either<ErrorInfo, Token> register(Register register) throws AuthenticationException {
        Either<ErrorInfo, Token> response = Either.left(errorMapper.getRn003());
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
                Token token = authProvider.generateToken(userSaved.get());
                response = Either.right(token);
            }else{
                response = Either.left(errorMapper.getRn000());
            }
        }
        return response;
    }

    @Override
    public Either<ErrorInfo, Token> login(Login login) {
        Either<ErrorInfo, Token> response;
        Optional<List<User>> userRegistered = userRepository.findByEmail(login.getEmail());
        if(!userRegistered.isPresent() || userRegistered.get().isEmpty()){
            response = Either.left(errorMapper.getRn004());
        }
        else if(!passwordEncoder.matches(login.getPassword(), userRegistered.get().get(0).getPassword())){
            response = Either.left(errorMapper.getRn005());
        }else{
                Token token = authProvider.generateToken(userRegistered.get().get(0));
                response = Either.right(token);
        }
        return response; 
    }
        @Override
    public Either<ErrorInfo, Boolean> sendValidationCode(String email) {
        Either<ErrorInfo, Boolean> response;
        Optional<List<User>> user = userRepository.findByEmail(email);
        if(!user.isPresent() || user.get().isEmpty()){
            response = Either.left(errorMapper.getRn004());
        }else{
            String code = generateSixDigitCode();
            Optional<UserToken> userToken = userTokenRepository.save(UserToken.builder()
            .token(code)                       
            .userId(user.get().get(0).getId())
            .build());
            if(userToken.isEmpty()){
                return Either.left(errorMapper.getRn000());

            }else{
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Email Validation");
                mailMessage.setText("Your validation code is: " + code);
                mailSender.send(mailMessage);
                response = Either.right(true);
            }
        }
        return response;
    }
    private String generateSixDigitCode() {
        int code = secureRandom.nextInt(1000000);
        return String.format("%06d", code);
    }
}