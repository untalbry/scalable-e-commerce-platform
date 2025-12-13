package com.binarybrains.userservice.infrastructure.jpa.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.binarybrains.userservice.core.entity.UserToken;
import com.binarybrains.userservice.core.ports.output.UserTokenRepository;
import com.binarybrains.userservice.infrastructure.jpa.entity.UserTokenJpa;
import com.binarybrains.userservice.infrastructure.jpa.repository.UserTokenJpaRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserTokenDao implements UserTokenRepository {
    UserTokenJpaRepository userTokenJpaRepository;

    @Override
    public Optional<UserToken> save(UserToken userToken) {
        return Optional.of(userTokenJpaRepository.save(UserTokenJpa.fromEntity(userToken)).toEntity()); 
    }

    
}
