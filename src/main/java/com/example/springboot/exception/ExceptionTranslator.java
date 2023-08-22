package com.example.springboot.exception;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.view_model.SignupVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.*;

@RestControllerAdvice
public class ExceptionTranslator {
    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleAMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        String errorMessageName = result.getAllErrors().get(0).getDefaultMessage();
        String errorField = ((FieldError)  result.getAllErrors().get(0)).getField();

        if(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.name().equals(errorMessageName)){
            String loginName = ((SignupVM) Objects.requireNonNull(result.getTarget())).getLoginName();
            response.put(ERROR_CODE_KEY, ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getMessage(), loginName));
        }
        else if(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.name().equals(errorMessageName)){
            String emailAddress = ((SignupVM) Objects.requireNonNull(result.getTarget())).getEmailAddress();
            response.put(ERROR_CODE_KEY, ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getMessage(), emailAddress));
        }
        else if (ErrorMessage.COMMON_FIELD_REQUIRED.name().equals(errorMessageName)){
            response.put(ERROR_CODE_KEY, ErrorMessage.COMMON_FIELD_REQUIRED.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.COMMON_FIELD_REQUIRED.getMessage(), errorField));
        }
        else {
            // Message of ErrorMessage do not have any argument
            Arrays.asList(ErrorMessage.values()).forEach(
                    (errorMessage -> {
                        if (errorMessage.name().equals(errorMessageName)){
                            response.put(ERROR_CODE_KEY, errorMessage.getErrorCode());
                            response.put(MESSAGE_KEY, errorMessage.getMessage());
                        }
                    })
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException exception){
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, "NOT_FOUND");
        response.put(MESSAGE_KEY, String.format("Can not find user with login name %s", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(IOException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("AuthenticationException");
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllException() {
//        ErrorMessage errorMessage = ErrorMessage.COMMON_INTERNAL_SERVER_ERROR;
//        LinkedHashMap<String, String> response = new LinkedHashMap<>();
//        response.put(ERROR_CODE_KEY, errorMessage.getErrorCode());
//        response.put(MESSAGE_KEY, errorMessage.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

}
