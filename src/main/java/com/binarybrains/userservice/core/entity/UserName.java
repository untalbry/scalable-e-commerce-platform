package com.binarybrains.userservice.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserName {
    private Integer id;
    private String name;
}
