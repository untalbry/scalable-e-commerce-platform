package com.binarybrains.userservice.infrastructure.rest.dto;

import com.binarybrains.userservice.core.entity.UserName;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Builder
public class UserNameDto {
    private Integer id;
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
