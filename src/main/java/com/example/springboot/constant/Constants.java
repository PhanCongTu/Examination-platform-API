package com.example.springboot.constant;

public final class Constants {
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String MESSAGE_KEY = "message";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String REQUIRED_PARAMETER = "REQUIRED_PARAMETER";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String NOT_ACCEPTABLE = "NOT_ACCEPTABLE";
    public static final String LOGIN_NAME_REGEX = "^([a-zA-Z0-9._-]{6,16}$)";
    public static final String PASSWORD_REGEX = "^[!-~]{8,20}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,62}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+$";
    private Constants(){
    }
}
