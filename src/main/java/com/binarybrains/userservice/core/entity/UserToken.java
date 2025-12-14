package com.binarybrains.userservice.core.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserToken {
     private Integer id;
    private Integer userId;
    private String token;
    private LocalDateTime createdAt;
}
