package com.example.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuestionGroupNotFoundException extends RuntimeException {
    public QuestionGroupNotFoundException() {
        super();
    }
}
