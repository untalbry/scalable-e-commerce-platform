package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.utils.enums.TokenType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(name ="Token", description = "DTO for authentication token")
public class TokenDto {
    @NotNull(message = "Token cannot be null")
    @Schema(description = "Authentication token", example = "Bearer abcdef12345")
    public String token;
    @Schema(description = "Type of the token", example = "BEARER")
    public TokenType tokenType = TokenType.BEARER;
    @Schema(description = "Indicates if the token is revoked", example = "false")
    public boolean revoked;
    @Schema(description = "Indicates if the token is expired", example = "false")
    public boolean expired;
    @NotNull(message = "Email cannot be null")
    @Schema(description = "Email associated with the token", example = "johnsmail@gmail.com")
    public UserDto userDto; 
}
