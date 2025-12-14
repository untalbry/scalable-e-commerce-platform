package com.binarybrains.userservice.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.binarybrains.userservice.infrastructure.jpa.entity.UserJpa;
import com.binarybrains.userservice.infrastructure.jpa.repository.UserJpaRepository;
import com.binarybrains.userservice.infrastructure.rest.dto.UserEmailDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegratonTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private UserJpa testUser;

    @BeforeEach
    void setUp() {
        testUser = UserJpa.builder()
                .name("John Doe")
                .email("john@example.com")
                .number("+1234567890")
                .password("password123")
                .build();
        testUser = userJpaRepository.save(testUser);
        entityManager.flush();
    }
    @Test
    @WithMockUser
    void shouldUpdateEmailAndVerifyBothConditions() throws Exception {
        Integer userId = testUser.getId();
        String originalEmail = testUser.getEmail();
        String newEmail = "updated@example.com";
        UserEmailDto userEmailDto = UserEmailDto.builder()
                .id(userId)
                .email(newEmail)
                .build();

        mockMvc.perform(patch("/api/user/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEmailDto)))
                .andExpect(status().isOk())
                .andReturn();
        //Verified user email updated
        entityManager.clear();
        UserJpa updatedUser = userJpaRepository.findById(userId).orElse(null);
        assertNotNull(updatedUser, "User should exist after email update");
        assertNotEquals(originalEmail, updatedUser.getEmail(), "Email should be different from original");
        assertEquals(newEmail, updatedUser.getEmail(), "Email should be updated to new email");
        //Verified email verification flag should be false after update
        String verifyQuery = "SELECT st_email_verified FROM ec01_users WHERE id_user = :id";
        Boolean isVerified = (Boolean) entityManager.createNativeQuery(verifyQuery)
                .setParameter("id", userId)
                .getSingleResult();
        assertFalse(isVerified, "Email verification flag should be false after update");
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUpdatingEmailForNonExistentUser() throws Exception {
        Integer nonExistentUserId = 9999;
        UserEmailDto userEmailDto = UserEmailDto.builder()
                .id(nonExistentUserId)
                .email("fake@example.com")
                .build();

        mockMvc.perform(patch("/api/user/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEmailDto)))
                .andExpect(status().is4xxClientError());
    }
}
