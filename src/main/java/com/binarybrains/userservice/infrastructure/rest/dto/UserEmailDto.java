package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.UserEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(name = "User email")
public class UserEmailDto {
    @NotBlank
    @Schema(
            description = "User id",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final Integer id;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Schema(
            description = "Email to be validated",
            example = "mail@mail.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String email;
    public static UserEmailDto fromEntity(UserEmail userEmail){
        return UserEmailDto.builder()
                .id(userEmail.getId())
                .email(userEmail.getEmail())
                .build();
    }
    public UserEmail toEntity(){
        return UserEmail.builder()
                .id(this.id)
                .email(this.email)
                .build();
    }
}
