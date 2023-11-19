package com.example.springboot.constant;

import lombok.*;

@Getter
public enum ErrorMessage {
    COMMON_FIELD_REQUIRED(Constants.REQUIRED_PARAMETER, "%s is required."),
    COMMON_INTERNAL_SERVER_ERROR(Constants.INTERNAL_SERVER_ERROR, "Undefined error."),
    COMMON_JSON_BODY_MALFORMED(Constants.BAD_REQUEST,"JSON body is malformed."),
    COMMON_USER_NOT_FOUND(Constants.NOT_FOUND,"Can not find user"),
    COMMON_VALUE_MUST_BE_NUMBER(Constants.BAD_REQUEST, "The input value must be a number."),


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

    CHANGE_PASSWORD_WRONG_OLD_PASSWORD(Constants.BAD_REQUEST, "Old password is incorrect."),

    CLASS_CODE_DUPLICATE(Constants.BAD_REQUEST, "The code of Classroom already exists."),
    CLASSROOM_NOT_FOUND(Constants.NOT_FOUND, "Can not find classroom."),

    STUDENT_NOT_FOUND(Constants.NOT_FOUND, "Can not find student."),

    QUESTION_GROUP_CODE_DUPLICATE(Constants.BAD_REQUEST, "The code of question group already exists."),
    QUESTION_GROUP_NOT_FOUND(Constants.NOT_FOUND, "Can not find question group."),
    QUESTION_GROUP_REQUIRE(Constants.BAD_REQUEST, "Question group is require."),

    QUESTION_CREATE_MUST_HAVE_ONE_TRUE_ANSWER(Constants.BAD_REQUEST, "There must have one only correct answer."),
    QUESTION_CREATE_ANSWER_CONTENT_REQUIRED(Constants.BAD_REQUEST, "answerContent is required."),
    QUESTION_NOT_FOUND(Constants.NOT_FOUND, "Can not find question."),

    MULTIPLE_CHOICE_TEST_START_DATE_INVALID(Constants.BAD_REQUEST, "Start date is not valid."),
    MULTIPLE_CHOICE_TEST_END_DATE_INVALID(Constants.BAD_REQUEST, "End date is not valid."),
    MULTIPLE_CHOICE_TEST_DATE_INVALID(Constants.BAD_REQUEST, "The endDate must be after the startDate."),
    MULTIPLE_CHOICE_TESTING_TIME_INVALID(Constants.BAD_REQUEST, "The testing time is not valid."),
    MULTIPLE_CHOICE_NOT_FOUND(Constants.NOT_FOUND, "Can not find the multiple choice test"),
    NOT_ENOUGH_QUESTION(Constants.BAD_REQUEST, "Do not enough question in question group with name is %s"),
    QUESTION_NUMBER_REQUIRE(Constants.NOT_FOUND, "Number of question is require."),
    MULTIPLE_CHOICE_TEST_QUESTION_SOURCE_INVALID(Constants.BAD_REQUEST, "The questions of the test is missing or duplicate source."),
    MULTIPLE_CHOICE_TEST_DELETE_STARTED_TEST(Constants.BAD_REQUEST, "Cannot delete this test. It has been started."),
    MULTIPLE_CHOICE_TEST_UPDATE_STARTED_TEST(Constants.BAD_REQUEST, "Cannot update this test. It has been started.")
    ;

    private String errorCode;
    private String message;
    ErrorMessage(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
