package com.example.springboot.exception;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.SignUpDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class ExceptionTranslator {
    

    /**
     * Exception handling when the json body in request is malformed
     *
     * @param exception : HttpMessageNotReadableException
     * @return : The response entity
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.COMMON_JSON_BODY_MALFORMED.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.COMMON_JSON_BODY_MALFORMED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    /**
     * Exception handling when input request parameter is invalid.
     *
     * @param ex: MethodArgumentNotValidException
     * @return : The response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleAMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        String errorMessageName = result.getAllErrors().get(0).getDefaultMessage();
        String errorField = ((FieldError) result.getAllErrors().get(0)).getField();
        String errorCode = "";
        String message = "";
        if (ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.name().equals(errorMessageName)) {
            String loginName = ((SignUpDTO) Objects.requireNonNull(result.getTarget())).getLoginName();
            errorCode = ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getErrorCode();
            message = String.format(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getMessage(), loginName);
        } else if (ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.name().equals(errorMessageName)) {
            String emailAddress = ((SignUpDTO) Objects.requireNonNull(result.getTarget())).getEmailAddress();
            errorCode = ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getErrorCode();
            message = String.format(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getMessage(), emailAddress);
        } else if (ErrorMessage.COMMON_FIELD_REQUIRED.name().equals(errorMessageName)) {
            errorCode = ErrorMessage.COMMON_FIELD_REQUIRED.getErrorCode();
            message = String.format(ErrorMessage.COMMON_FIELD_REQUIRED.getMessage(), errorField);
        } else {


            // Message of ErrorMessage do not have any argument
            Arrays.asList(ErrorMessage.values()).forEach(
                    (errorMessage -> {
                        if (errorMessage.name().equals(errorMessageName)) {
                            response.put(Constants.ERROR_CODE_KEY, errorMessage.getErrorCode());
                            response.put(Constants.MESSAGE_KEY, errorMessage.getMessage());

                        }
                    })
            );
        }
        if (response.isEmpty()){
            response.put(Constants.ERROR_CODE_KEY, errorCode);
            response.put(Constants.MESSAGE_KEY, message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     *
     * Exception handling when username was not found.
     *
     * @param exception: UsernameNotFoundException
     * @return : The response entity
     */
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_NAME_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, String.format(ErrorMessage.LOGIN_NAME_NOT_FOUND.getMessage(), exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.COMMON_USER_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, String.format(ErrorMessage.COMMON_USER_NOT_FOUND.getMessage(), exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    /**
     * Exception handling when refresh token is wrong or expired
     *
     * @return : The response entity
     */
    @ExceptionHandler({RefreshTokenExpiredException.class, RefreshTokenNotFoundException.class})
    public ResponseEntity<?> handleRefreshTokenException() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when refresh token is wrong or expired
     *
     * @return : The response entity
     */
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<?> handleTokenException() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when the login name and password are wrong.
     *
     * @param exception : The BadCredentialsException
     * @return : The response entity
     */
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_BAD_CREDENTIALS.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_BAD_CREDENTIALS.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when the user does not have permissions to access the source.
     *
     * @param exception : AccessDeniedException
     * @return : The response entity
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_ACCESS_DENIED.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_ACCESS_DENIED.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when The JWT access token is Malformed
     *
     * @param malformedJwtException : malformedJwtException
     * @return : The response entity
     */
    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<?> handleSignatureException(MalformedJwtException malformedJwtException) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    /**
     * Exception handling when The JWT access token is about Signature
     *
     * @param SignatureException : SignatureException
     * @return : The response entity
     */
    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<?> handleSignatureException(SignatureException SignatureException) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.LOGIN_TOKEN_INVALID.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({InValidUserStatusException.class})
    public ResponseEntity<?> handleInValidUserStatusException(InValidUserStatusException inValidUserStatusException) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.VERIFY_INVALID_STATUS.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.VERIFY_INVALID_STATUS.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({EmailAddressVerifiedByAnotherUser.class})
    public ResponseEntity<?> handleEmailAddressVerifiedByAnotherUserException(EmailAddressVerifiedByAnotherUser ex) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.VERIFY_EMAIL_VERIFIED_BY_ANOTHER_USER.getErrorCode());
        response.put(Constants.MESSAGE_KEY, String.format(ErrorMessage.VERIFY_EMAIL_VERIFIED_BY_ANOTHER_USER.getMessage(), ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    // Uncomment bên dưới khi project hoàn thành

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllException(Exception ex) {
//        ErrorMessage errorMessage = ErrorMessage.COMMON_INTERNAL_SERVER_ERROR;
//        LinkedHashMap<String, String> response = new LinkedHashMap<>();
//        response.put(Constants.ERROR_CODE_KEY, errorMessage.getErrorCode());
//        response.put(Constants.MESSAGE_KEY, errorMessage.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

}
