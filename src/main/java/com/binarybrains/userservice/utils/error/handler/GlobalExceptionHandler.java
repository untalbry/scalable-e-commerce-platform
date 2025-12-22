package com.binarybrains.userservice.utils.error.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.binarybrains.userservice.utils.error.ErrorGlobalMapper;
import com.binarybrains.userservice.utils.error.ErrorInfo;
import com.binarybrains.userservice.utils.error.ErrorResponse;
import com.binarybrains.userservice.utils.error.UserException;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorGlobalMapper mapper; 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorInfo error = mapper.getRn002();
        error.setRuta(Thread.currentThread().getStackTrace()[2].getClassName());
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerGenericException(Exception ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Ocurrio un error inesperado: " + ex.getLocalizedMessage()));
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorInfo> HandlerUserException(UserException ex){
        ErrorInfo error = ex.getErrorInfo();
        error.setRuta(Thread.currentThread().getStackTrace()[2].getClassName());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if("RN004".equals(error.getCode())){
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(error);
    }
}