package com.binarybrains.userservice.core.ports.input;

import com.binarybrains.userservice.core.entity.EmailVerification;
import com.binarybrains.userservice.core.entity.Login;
import com.binarybrains.userservice.core.entity.Register;
import com.binarybrains.userservice.core.entity.Token;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;

public interface  AuthService {
    Either<ErrorInfo, Token> register(Register register); 
    Either<ErrorInfo, Token> login(Login login);
    Either<ErrorInfo, Boolean> sendValidationCode(String email);
    Either<ErrorInfo, Boolean> confirmValidationCode (EmailVerification emailVerification);
}
