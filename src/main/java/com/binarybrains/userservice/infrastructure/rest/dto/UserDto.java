package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(name = "User", description = "DTO for user information.")
public class UserDto {
    @NotNull(message = "Name cannot be null")
    @Schema(description = "Users name", example = "John Doe")
    private String name;
    @NotNull(message = "Email cannot be null")
    @Schema(description = "Users email", example = "johnsmail@gmail.com")
    private String email;
    @Schema(description = "Users phone number", example = "+1234567890")
    private String number; 

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .number(user.getNumber())
            .build();
    }
    public User toEntity() {
        return User.builder()
            .name(this.name)
            .email(this.email)
            .number(this.number)
            .build();
    }
}

