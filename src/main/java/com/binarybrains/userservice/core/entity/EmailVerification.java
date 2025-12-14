package com.binarybrains.userservice.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailVerification {
    private String email;
    private String token;
}
