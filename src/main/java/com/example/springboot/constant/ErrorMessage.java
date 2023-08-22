package com.example.springboot.constant;

import lombok.*;

@Getter
public enum ErrorMessage {
    COMMON_FIELD_REQUIRED(Constants.REQUIRED_PARAMETER, "%s is required"),
    COMMON_INTERNAL_SERVER_ERROR(Constants.INTERNAL_SERVER_ERROR, "Undefined error"),

    SIGNUP_LOGIN_NAME_DUPLICATE(Constants.USER_ALREADY_EXISTS, "User with loginName %s already exists"),
    SIGNUP_LOGIN_NAME_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of loginName - Must be made of letter, number, '-', '_', and/or '.' . Length must be between 4 and 16 chars"),
    SIGNUP_PASSWORD_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of password - Must be made of letters, numbers, and/or '!' - '~'. Length must be between 8 and 20 chars"),
    SIGNUP_EMAIL_ADDRESS_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of email address - Must be made of letters, numbers, contains '@' and/or '_' - '-'. " +
            "Dot isn't allowed at the start and end of the local part. Consecutive dots aren't allowed. a maximum of 64 characters are allowed."),
    SIGNUP_EMAIL_ADDRESS_DUPLICATE(Constants.USER_ALREADY_EXISTS, "User with email address %s already exists")
    ;

    private String errorCode;
    private String message;
    ErrorMessage(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
