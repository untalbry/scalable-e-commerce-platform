package com.binarybrains.userservice.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binarybrains.userservice.infrastructure.jpa.entity.TokenJpa;
@Repository
public interface  TokenJpaRepository extends JpaRepository<TokenJpa, Long> {
    
}
