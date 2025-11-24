package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.Register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(name ="Register", description = "DTO to Register a user")
public class RegisterDto {
    private String name;
    @NotNull(message = "Email cannot be null")
    @Schema(description = "Users email", example = "johnsmail@gmail.com")
    private String email; 
    @Schema(description = "Users number", example = "+55123456789")
    private String number;
    @NotNull(message = "Password cannot be null")
    @Schema(description = "Users password", example = "password123")
    private String password; 

    public Register toEntity() {
        return Register.builder()
            .name(this.name)
            .email(this.email)
            .number(this.number)
            .password(this.password)
            .build();
    }
}
