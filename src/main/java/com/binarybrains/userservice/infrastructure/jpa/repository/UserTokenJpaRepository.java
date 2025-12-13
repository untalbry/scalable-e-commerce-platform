package com.binarybrains.userservice.infrastructure.jpa.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.binarybrains.userservice.infrastructure.jpa.entity.UserTokenJpa;

public interface  UserTokenJpaRepository extends JpaRepository<UserTokenJpa, Integer>{
    
}
