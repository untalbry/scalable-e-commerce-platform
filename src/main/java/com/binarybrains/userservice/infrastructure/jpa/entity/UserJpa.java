package com.binarybrains.userservice.infrastructure.jpa.entity;

import com.binarybrains.userservice.core.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "ec01_users")
@Entity
public class UserJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="id_user")
    private Integer id;
    @Column(name="tx_name")
    private String name;
    @Column(name="tx_email")
    private String email;
    @Column(name="tx_phone_number")
    private String number;
    @Column(name="tx_password")
    private String password;

    public User toEntity() {
        return User.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .number(this.number)
            .password(this.password)
            .build();
    }
    public static UserJpa fromEntity(User user) {
        return UserJpa.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .number(user.getNumber())
            .password(user.getPassword())
            .build();
    }
}