package com.binarybrains.userservice.external.jpa.entity;

import com.binarybrains.userservice.core.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ec01-users")
public class UserJpa {
    @Id
    @Column(name="id_user")
    private Integer id;
    @Column(name="tx_name")
    private String name;
    @Column(name="tx_email")
    private String email;
    @Column(name="tx_phone_number")
    private String number;
    public User toEntity() {
        return User.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .number(this.number)
            .build();
    }
    public static UserJpa fromEntity(UserJpa userJpa) {
        return UserJpa.builder()
            .id(userJpa.getId())
            .name(userJpa.getName())
            .email(userJpa.getEmail())
            .number(userJpa.getNumber())
            .build();
    }
}