package com.binarybrains.userservice.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.core.ports.output.UserTokenRepository;
import com.binarybrains.userservice.infrastructure.rest.dto.EmailVerificationConfirmDto;
import com.binarybrains.userservice.infrastructure.rest.dto.EmailVerificationRequestDto;
import com.binarybrains.userservice.infrastructure.rest.dto.RegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
class EmailValidationFlowE2ETest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Mock
    private JavaMailSender mailSender;
    
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserRepository userRepository;

    
    private static final String TEST_EMAIL = "testmail@gmail.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NAME = "Test";
    private static final String TEST_NUMBER = "+5512345678";
    
    @BeforeEach
    void setUp() throws Exception {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        RegisterDto registerDto = RegisterDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .number(TEST_NUMBER)
                .build();
        
        String registerBody = objectMapper.writeValueAsString(registerDto);
        userRepository.deleteAllByEmail(TEST_EMAIL);
        mockMvc.perform(post("/api/auth/register")
                    .content(registerBody)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
 
    }
    
    @Test
    void shouldValidateUsersEmail() throws Exception{
        // Step 1: Send verification code request
        EmailVerificationRequestDto emailVerificationRequest = EmailVerificationRequestDto.builder()
                .email(TEST_EMAIL)
                .build();
        String verificationRequestBody = objectMapper.writeValueAsString(emailVerificationRequest);
        
        mockMvc.perform(post("/api/auth/email-verification")
                .content(verificationRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // Step 2: Get the generated token from the database
        String generatedToken = userTokenRepository.findByUserEmail(TEST_EMAIL)
                .orElseThrow(() -> new AssertionError("Token not found"))
                .getFirst()
                .getToken();
        
        // Step 3: Confirm the email verification with the token
        EmailVerificationConfirmDto emailVerificationConfirm = EmailVerificationConfirmDto.builder()
                .email(TEST_EMAIL)
                .token(generatedToken)
                .build();
        String confirmRequestBody = objectMapper.writeValueAsString(emailVerificationConfirm);
        
        mockMvc.perform(post("/api/auth/email-validation/confirm")
                .content(confirmRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
