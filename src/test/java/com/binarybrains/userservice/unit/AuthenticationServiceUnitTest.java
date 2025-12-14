package com.binarybrains.userservice.unit;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.binarybrains.userservice.core.entity.Login;
import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.implementation.AuthBs;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;
import com.binarybrains.userservice.utils.error.ErrorType;

import io.vavr.collection.List;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceUnitTest {
    
    @InjectMocks 
    private AuthBs authBs;
    @Mock 
    private UserRepository userRepository;
    @Mock
	private ErrorGlobalMapper errorMapper;
    @Mock 
    private AuthProvider authProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test 
    void shouldReturnErrorIfUserAlreadyExists() {
        Register register= Register.builder()
                        .email("email@email.com")
                        .build();

        ErrorInfo err = ErrorInfo.builder()
						.code("RN003")
						.message("Not found")
						.ruta("com.binarybrains.userservice.core.implementation")
						.type(ErrorType.REQUEST)
						.build();
        when(errorMapper.getRn003()).thenReturn(err);
        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.of(List.of(User.builder().email("email@email.com").build()).toJavaList()));
        var result = authBs.register(register);
        assertTrue(result.isLeft());
        assertEquals("RN003", result.getLeft().getCode());
    }

    @Test
    void shouldGenerateJwtOnSuccessfulRegister() {
        Register register= Register.builder()
                        .email("new@email.com")
                        .name("New User")
                        .password("plainPassword")
                        .number("1234567890")
                        .build();

        when(userRepository.findByEmail(register.getEmail())).thenReturn(Optional.of(java.util.Collections.emptyList()));
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        User savedUser = User.builder()
                        .id(1)
                        .name(register.getName())
                        .email(register.getEmail())
                        .number(register.getNumber())
                        .password("encodedPassword")
                        .build();
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(savedUser));

        Token expectedToken = Token.builder().token("token-123").user(savedUser).build();
        when(authProvider.generateToken(savedUser)).thenReturn(expectedToken);

        var result = authBs.register(register);

        assertTrue(result.isRight());
        assertEquals("token-123", result.get().getToken());

    }
    @Test
    void shouldGenerateJwtOnSuccessfulLogin(){

        Login login = Login.builder()
            .email("new@email.com")
            .password("plainPassword")
            .build();
        User registeredUser = User.builder()
                        .password("encodedPassword")
                        .build();
        Token expectedToken = Token.builder().token("token-123").user(registeredUser).build();
        
        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(java.util.List.of(registeredUser)));
        when(passwordEncoder.matches(login.getPassword(), registeredUser.getPassword())).thenReturn(true);
        when(authProvider.generateToken(registeredUser)).thenReturn(expectedToken);
        var result = authBs.login(login);
        assertTrue(result.isRight());
        assertEquals("token-123", result.get().getToken());
    }

}