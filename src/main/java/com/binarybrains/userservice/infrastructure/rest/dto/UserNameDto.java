package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.UserName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Builder
public class UserNameDto {
    @Schema(
            description = "User id",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Integer id;

    @Schema(
        description = "New name of the user",
        example = "John Doe",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Name cannot be blank")
    private String name;

    public static UserNameDto fromEntity(UserName userName){
        return UserNameDto.builder()
                .id(userName.getId())
                .name(userName.getName())
                .build();
    }
    public UserName toEntity(){
        return UserName.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
