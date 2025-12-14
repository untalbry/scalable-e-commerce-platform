package com.binarybrains.userservice.infrastructure.jpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.infrastructure.jpa.entity.UserJpa;
import com.binarybrains.userservice.infrastructure.jpa.repository.UserJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserDao implements UserRepository{
    private final UserJpaRepository userJpaRepository;
    private final EntityManager entityManagerReading;
    private final EntityManager entityManager;
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM ec01_users WHERE tx_email =:email";
    private static final String UPDATE_USER_EMAIL_AS_VERIFIED_BY_EMAIL = """
        update ec01_users ec01 
        set st_email_verified = true
        where ec01.tx_email = :email
    """;
    private static final String DELETE_USER_BY_EMAIL = """
        delete from ec01_users ec01 where ec01.tx_email = :email
    """;
    private static final String UPDATE_USER_EMAIL = """
            update ec01_users ec01
            set tx_email =:email,
            st_email_verified = false
            where id_user =:id
            """;
    @Override
    public Optional<User> findById(Integer id) {
        return userJpaRepository.findById(id).map(UserJpa::toEntity);
    }

    @Override
    public Optional<User> save(User user) {
       return Optional.of(userJpaRepository.save(UserJpa.fromEntity(user)).toEntity());
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackOn = Exception.class)
    public Optional<List<User>> findByEmail(String email) {
        return Optional.of((List<User>) entityManagerReading.createNativeQuery(GET_USER_BY_EMAIL, UserJpa.class)
            .setParameter("email", email)
            .getResultList()
            .stream()
            .map(object -> ((UserJpa) object).toEntity()).toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean updateEmailValidationAsVerifiedByEmail(String email) {
        int rowsUpdated = entityManager.createNativeQuery(UPDATE_USER_EMAIL_AS_VERIFIED_BY_EMAIL)
        .setParameter("email", email)
        .executeUpdate();
        return rowsUpdated>=0;
    }



    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAllByEmail(String email) {
        entityManager.createNativeQuery(DELETE_USER_BY_EMAIL)
        .setParameter("email", email)
        .executeUpdate();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean updateEmailById(Integer userId, String email) {
        int rowAffected = entityManager.createNativeQuery(UPDATE_USER_EMAIL)
                .setParameter("id", userId)
                .setParameter("email", email)
                .executeUpdate();
        return rowAffected > 0;
    }
}

