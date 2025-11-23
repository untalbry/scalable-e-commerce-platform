package com.binarybrains.userservice.core.ports.input;

import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;

public interface  AuthService {
    Either<ErrorInfo, String> register(Register register); 
}
