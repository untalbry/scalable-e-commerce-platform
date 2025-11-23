package com.binarybrains.userservice.core.ports.input;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;

public interface UserService {
    Either<ErrorInfo, User> getById(Integer id);   
}
