package com.binarybrains.userservice.core.implementation;



import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.core.entity.UserEmail;
import com.binarybrains.userservice.core.entity.UserName;
import com.binarybrains.userservice.core.ports.input.UserService;
import com.binarybrains.userservice.core.ports.output.UserRepository;
import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class UserBs implements UserService{
    private final  UserRepository userRepository;
    private final ErrorGlobalMapper errorMapper;

    @Override
    public Either<ErrorInfo, User> getById(Integer id) {
        return userRepository.findById(id)
                .<Either<ErrorInfo, User>>map(Either::right)
                .orElseGet(() -> Either.left(errorMapper.getRn004()));
    }

    @Override
    public Either<ErrorInfo, Boolean> updateEmail(UserEmail userEmail) {
        Either<ErrorInfo, Boolean> result;
        Optional<User> user = userRepository.findById(userEmail.getId());
        if(user.isEmpty()){
            result = Either.left(errorMapper.getRn004());
        }else if(Boolean.FALSE.equals(userRepository.updateEmailById(user.get().getId(), userEmail.getEmail()))){
            result = Either.left(errorMapper.getRn000());
        }else{
            result = Either.right(true);
        }
        return result;
    }

    @Override
    public Either<ErrorInfo, Boolean> updateName(UserName userName) {
        Either<ErrorInfo, Boolean> result; 
        Optional<User> user = userRepository.findById(userName.getId());
        if(user.isEmpty()){
            result = Either.left(errorMapper.getRn004());
        }else if (Boolean.FALSE.equals(userRepository.updateNameById(userName.getId(), userName.getName()))){
            result = Either.left(errorMapper.getRn000()); 
        }else{
            result = Either.right(true); 
        }
        return result; 
    }
}