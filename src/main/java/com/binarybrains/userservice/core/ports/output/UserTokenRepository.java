package com.binarybrains.userservice.core.ports.output;

import java.util.List;
import java.util.Optional;

import com.binarybrains.userservice.core.entity.UserToken;

public interface  UserTokenRepository {
    Optional<UserToken> save(UserToken userToken);
    Optional<List<UserToken>> findByUserEmail(String email);
    void deleteAllByEmail(String email);
}
