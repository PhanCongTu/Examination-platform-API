package com.example.springboot.constant;

public final class Constants {
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
    public static final String LOGIN_NAME_REGEX = "^([a-zA-Z0-9._-]{4,16}$)";
    public static final String PASSWORD_REGEX = "^[!-~]{8,20}$";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private Constants(){
    }
}
