package com.example.springboot.constant;

import lombok.*;

@Getter
public enum ErrorMessage {
    COMMON_FIELD_REQUIRED(Constants.REQUIRED_PARAMETER, "%s is required."),
    COMMON_INTERNAL_SERVER_ERROR(Constants.INTERNAL_SERVER_ERROR, "Undefined error."),
    COMMON_JSON_BODY_MALFORMED(Constants.BAD_REQUEST,"JSON body is malformed."),
    COMMON_USER_NOT_FOUND(Constants.NOT_FOUND,"Can not find user"),


    SIGNUP_LOGIN_NAME_DUPLICATE(Constants.USER_ALREADY_EXISTS, "User with loginName %s already exists."),
    SIGNUP_LOGIN_NAME_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of loginName - Must be made of letter, number, '-', '_', and/or '.' . Length must be between 6 and 16 chars."),
    SIGNUP_PASSWORD_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of password - Must be made of letters, numbers, and/or '!' - '~'. Length must be between 8 and 20 chars."),
    SIGNUP_EMAIL_ADDRESS_INVALID_CHARACTER(Constants.INVALID_PARAMETER, "There are validation errors of email address - Must be made of letters, numbers, contains '@' and/or '_' - '-'. " +
            "Dot isn't allowed at the start and end of the local part. Consecutive dots aren't allowed. a maximum of 64 characters are allowed."),
    SIGNUP_EMAIL_ADDRESS_DUPLICATE(Constants.USER_ALREADY_EXISTS, "User with email address %s already exists."),

    LOGIN_NAME_NOT_FOUND(Constants.NOT_FOUND, "Can not find user with login name %s."),
    LOGIN_BAD_CREDENTIALS(Constants.BAD_REQUEST, "Wrong login or password."),
    LOGIN_ACCESS_DENIED(Constants.FORBIDDEN, "Not authorized."),
    LOGIN_TOKEN_INVALID(Constants.FORBIDDEN, "Access token or refresh token is invalid or expired."),

    VERIFY_NOT_ACCEPTABLE(Constants.NOT_ACCEPTABLE, "Your verification code is not valid."),
    VERIFY_INVALID_STATUS(Constants.BAD_REQUEST, "User status is not enable to verify."),
    VERIFY_EMAIL_VERIFIED_BY_ANOTHER_USER(Constants.BAD_REQUEST, "Email address %s has been verified by another user."),

    RESET_PASSWORD_NOT_ACCEPTABLE(Constants.NOT_ACCEPTABLE, "Your reset code is not valid."),

    ;

    private String errorCode;
    private String message;
    ErrorMessage(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
