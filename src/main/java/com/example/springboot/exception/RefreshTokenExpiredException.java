package com.example.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenExpiredException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public RefreshTokenExpiredException(String refreshToken, String message){
        super(String.format("Failed for [%s]: %s", refreshToken, message));
    }
}
