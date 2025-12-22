package com.binarybrains.userservice.core.ports.input;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.entity.UserEmail;
import com.binarybrains.userservice.core.entity.UserName;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;

public interface UserService {
    Either<ErrorInfo, User> getById(Integer id);
    Either<ErrorInfo, Boolean> updateEmail(UserEmail userEmail);
    Either<ErrorInfo, Boolean> updateName(UserName userName);
}
