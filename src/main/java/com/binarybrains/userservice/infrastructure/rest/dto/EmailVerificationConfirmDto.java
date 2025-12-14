package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.EmailVerification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(
    name = "EmailVerificationConfirm",
    description = "DTO used to confirm the email verification process. " +
                  "It contains the email address to be verified and the verification token " +
                  "sent to the user's email."
)
public class EmailVerificationConfirmDto {
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Schema(
        description = "Email to be validated", 
        example = "mail@mail.com", 
        requiredMode = Schema.RequiredMode.REQUIRED)
    private final String email;
    @NotBlank(message = "Verification token cannot be blank")
    @Schema(
        description = "Email verification token",
        example = "A9F3K2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final String token; 
    public static EmailVerificationConfirmDto fromEntity(EmailVerification emailVerification){
        return EmailVerificationConfirmDto.builder()
        .email(emailVerification.getEmail())
        .token(emailVerification.getToken())
        .build();
        
    }
    public EmailVerification toEntity(){
        return EmailVerification.builder()
        .email(this.email)
        .token(this.token)
        .build();
    }
    

}
