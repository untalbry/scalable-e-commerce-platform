package com.binarybrains.userservice.infrastructure.jpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.binarybrains.userservice.core.entity.UserToken;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "ec06_user_tokens")
@Entity
public class UserTokenJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="id_user_token")
    private Integer id;
    @Column(name="fk_id_user")
    private Integer userId;
    @Column(name="tx_token")
    private String token;
    @Column(name="dt_created_at")
    private LocalDateTime createdAt;
    
    public static UserTokenJpa fromEntity(UserToken userToken) {
        return UserTokenJpa.builder()
            .id(userToken.getId())
            .userId(userToken.getUserId())
            .token(userToken.getToken())
            .createdAt(LocalDateTime.now())
            .build();
    }
    public UserToken toEntity() {
        return UserToken.builder()
            .id(this.id)
            .userId(this.userId)
            .token(this.token)
            .createdAt(this.createdAt)
            .build();      
    }
}
