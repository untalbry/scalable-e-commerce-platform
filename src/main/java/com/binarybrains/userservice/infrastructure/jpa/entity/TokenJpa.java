package com.binarybrains.userservice.infrastructure.jpa.entity;

import com.binarybrains.userservice.utils.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ec06_tokens")
@Entity
public class TokenJpa {
    @Column(name="id_token")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id; 
    @Column(name="tx_token")
    public String token;
    @Column(name="tx_token_type")
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;
    @Column(name="st_revoked")
    public boolean revoked;
    @Column(name="st_expired")
    public boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_id_user", referencedColumnName = "id_user")
    public UserJpa userJpa;
    public static TokenJpa fromEntity(com.binarybrains.userservice.core.entity.Token token) {
        return TokenJpa.builder()
            .id(token.getId())
            .token(token.getToken())
            .tokenType(token.getTokenType())
            .revoked(token.isRevoked())
            .expired(token.isExpired())
            .userJpa(UserJpa.fromEntity(token.getUser()))
            .build();
    }
}
