package com.binarybrains.userservice.infrastructure.jpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.binarybrains.userservice.core.entity.UserToken;
import com.binarybrains.userservice.core.ports.output.UserTokenRepository;
import com.binarybrains.userservice.infrastructure.jpa.entity.UserTokenJpa;
import com.binarybrains.userservice.infrastructure.jpa.repository.UserTokenJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserTokenDao implements UserTokenRepository {
    private final UserTokenJpaRepository userTokenJpaRepository;
    private final EntityManager entityManagerReader;
    private final EntityManager entityManager; 
    private final static String QUERY_GET_TOKENS_BY_USER_EMAIL = """
        SELECT ec06.id_user_token, ec06.fk_id_user, ec06.tx_token, ec06.dt_created_at FROM ec06_user_tokens ec06 
        INNER JOIN ec01_users ec01 on 
        ec06.fk_id_user  =  (
            select id_user from ec01_users where tx_email = :email
        )
    """ ;
    private final static String QUERY_DELETE_TOKENS_BY_USER_EMAIL = """
        delete from ec06_user_tokens ec06 where ec06.fk_id_user = (
            select id_user from ec01_users where tx_email= :email
        )
    """;
    @Override
    public Optional<UserToken> save(UserToken userToken) {
        return Optional.of(userTokenJpaRepository.save(UserTokenJpa.fromEntity(userToken)).toEntity()); 
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<List<UserToken>> findByUserEmail(String email) {
        List<UserTokenJpa> tokens = entityManagerReader
        .createNativeQuery(QUERY_GET_TOKENS_BY_USER_EMAIL, UserTokenJpa.class)
        .setParameter("email", email)
        .getResultList();
        return Optional.of(tokens.stream().map(UserTokenJpa::toEntity).toList());
    }

    @Override
    public void deleteAllByEmail(String email) {
        entityManager.createNativeQuery(QUERY_DELETE_TOKENS_BY_USER_EMAIL)
        .setParameter("email", email);
    }

}
