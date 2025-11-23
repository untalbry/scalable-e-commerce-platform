package com.binarybrains.userservice.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binarybrains.userservice.infrastructure.jpa.entity.UserJpa;

@Repository
public interface  UserJpaRepository extends JpaRepository<UserJpa, Integer> {
    
}
