package com.binarybrains.userservice.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Register {
    private String name;
    private String email;
    private String number;
    private String password;
}
