package com.binarybrains.userservice.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {
    private Integer id;
    private String name;
    private String email;
    private String number;
}
