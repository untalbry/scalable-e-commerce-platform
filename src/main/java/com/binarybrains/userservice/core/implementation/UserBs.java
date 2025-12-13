package com.binarybrains.userservice.core.implementation;



import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.ports.input.UserService;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Service
@Primary
@AllArgsConstructor
public class UserBs implements UserService{
    private final  UserRepository userRepository;
    private final ErrorGlobalMapper errorMapper;

    @Override
    public Either<ErrorInfo, User> getById(Integer id) {
        return userRepository.findById(id)
                .<Either<ErrorInfo, User>>map(Either::right)
                .orElseGet(() -> Either.left(errorMapper.getRn004()));
    }

}