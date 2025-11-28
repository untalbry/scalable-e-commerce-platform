package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.Login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(name ="Login", description = "DTO to Login a user")
public class LoginDto {
    @Schema(description = "Users email", example = "user@mail.com")
    @NotNull(message = "Email cannot be null")
    String email;
    @Schema(description = "Users password", example = "password123")
    @NotNull(message = "Password cannot be null")
    String password;
    public static LoginDto fromEntity(TokenDto token, String email, String password) {
        return LoginDto.builder()
            .email(email)
            .password(password)
            .build();
    }
    public Login toEntity() {
        return Login.builder()
            .email(this.email)
            .password(this.password)
            .build();
    }
}
