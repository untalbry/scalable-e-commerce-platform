package com.binarybrains.userservice.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(
    name = "EmailVerificationRequest",
    description = "DTO used to request an email verification process. " +
                  "It contains the email address for which a verification code " +
                  "will be sent."
)
public class EmailVerificationRequestDto {
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Schema(description = "Email to be validated", example = "mail@mail.com")
    private final String email;

}
