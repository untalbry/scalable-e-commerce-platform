package com.binarybrains.userservice;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.implementation.AuthBs;
import com.binarybrains.userservice.core.ports.output.AuthProvider;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;
import com.binarybrains.userservice.utils.error.ErrorType;

import io.vavr.collection.List;

@ExtendWith(MockitoExtension.class)
public class AuthenticationUnitTest {
    
    @InjectMocks 
    private AuthBs authBs;
    @Mock 
    private UserRepository userRepository;
    @Mock
	private ErrorGlobalMapper errorMapper;
    @Mock 
    private AuthProvider authProvider;
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

}